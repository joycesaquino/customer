package controller

import (
	"context"
	"customer-api/internal/repository"
	"customer-api/internal/types"
	"encoding/json"
	"fmt"
	"github.com/caarlos0/env"
	"github.com/gin-gonic/gin"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	"io/ioutil"
	"log"
	"net/http"
)

type Config struct {
	ClientId     string `env:"GOOGLE_CLIENT_ID,required"`
	ClientSecret string `env:"GOOGLE_CLIENT_SECRET,required"`
	OAuthSecret  string `env:"OAUTH_SECRET,required"`
	RedirectURL  string `env:"REDIRECT_URL" envDefault:"http://localhost:8080/callback"`
}

type Oauth2Client struct {
	oauthConfig *oauth2.Config
	config      Config
	repository  *repository.CustomerRepository
}

func (login Oauth2Client) Login(c *gin.Context) {
	redirect := login.oauthConfig.AuthCodeURL(login.config.OAuthSecret)
	c.Redirect(http.StatusTemporaryRedirect, redirect)
}

func (login Oauth2Client) GoogleCallback(c *gin.Context) {
	ctx := c.Request.Context()
	user, err := login.getUser(ctx, c.Request.FormValue("state"), c.Request.FormValue("code"))
	if err != nil {
		fmt.Println(err.Error())
		c.Redirect(http.StatusTemporaryRedirect, "/")
		return
	}

	customer, err := login.repository.Create(ctx, &types.Customer{
		Name:  user.Name,
		Email: user.Email,
		Image: user.Image,
		OAuth: user.Token,
	})

	redirect := fmt.Sprintf("customer/%s", customer.Id.Hex())
	c.Redirect(http.StatusCreated, redirect)

	return
}

func (login Oauth2Client) getUser(ctx context.Context, state string, code string) (*types.User, error) {
	if state != login.config.OAuthSecret {
		return nil, fmt.Errorf("nvalid oauth state")
	}

	token, err := login.oauthConfig.Exchange(ctx, code)
	if err != nil {
		return nil, err
	}

	response, err := http.Get("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token.AccessToken)
	if err != nil {
		return nil, fmt.Errorf("failed getting user info: %s", err.Error())
	}

	defer response.Body.Close()
	contents, err := ioutil.ReadAll(response.Body)
	if err != nil {
		return nil, err
	}

	var user *types.User
	if err := json.Unmarshal(contents, &user); err != nil {
		return nil, err
	}
	user.Token.TokenType = token.TokenType
	user.Token.AccessToken = token.Extra("id_token")
	user.Token.RefreshToken = token.RefreshToken
	user.Token.Expiry = token.Expiry

	return user, nil
}

func Oauth2(repository *repository.CustomerRepository) *Oauth2Client {
	var config Config
	err := env.Parse(&config)
	if err != nil {
		log.Fatalf("Error on configure Oauth2 cliente. Error : %s", err)
	}

	return &Oauth2Client{
		&oauth2.Config{
			ClientID:     config.ClientId,
			ClientSecret: config.ClientSecret,
			Endpoint:     google.Endpoint,
			RedirectURL:  config.RedirectURL,
			Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email", "https://www.googleapis.com/auth/userinfo.profile"},
		}, config, repository,
	}
}
