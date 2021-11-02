package controller

import (
	"context"
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
}

type Oauth2Client struct {
	oauthConfig *oauth2.Config
	config      Config
}
type User struct {
	Id            string `json:"id"`
	Email         string `json:"email"`
	VerifiedEmail bool   `json:"verified_email"`
	Image         string `json:"picture"`
}

func (login Oauth2Client) Login(c *gin.Context) {
	redirect := login.oauthConfig.AuthCodeURL(login.config.OAuthSecret)
	c.Redirect(http.StatusTemporaryRedirect, redirect)
}

func (login Oauth2Client) GoogleCallback(c *gin.Context) {
	content, err := login.getUser(c.Request.Context(), c.Request.FormValue("state"), c.Request.FormValue("code"))
	if err != nil {
		fmt.Println(err.Error())
		c.Redirect(http.StatusTemporaryRedirect, "/")
		return
	}

	//TODO Use this user to persist in db and return logged user
	fmt.Printf("Content: %s\n", content)
	var user *User
	if err := json.Unmarshal(content, user); err != nil {
		return
	}
	return
}

func (login Oauth2Client) getUser(ctx context.Context, state string, code string) ([]byte, error) {
	if state != login.config.OAuthSecret {
		return nil, fmt.Errorf("invalid oauth state")
	}
	token, err := login.oauthConfig.Exchange(ctx, code)
	if err != nil {
		return nil, fmt.Errorf("code exchange failed: %s", err.Error())
	}
	response, err := http.Get("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + token.AccessToken)
	if err != nil {
		return nil, fmt.Errorf("failed getting user info: %s", err.Error())
	}
	defer response.Body.Close()
	log.Printf("Response %v", response.Body)
	contents, err := ioutil.ReadAll(response.Body)
	if err != nil {
		return nil, fmt.Errorf("failed reading response body: %s", err.Error())
	}
	return contents, nil
}

func Oauth2() *Oauth2Client {
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
			RedirectURL:  "http://localhost:8080/callback",
			Scopes:       []string{"https://www.googleapis.com/auth/userinfo.email"},
		}, config,
	}
}
