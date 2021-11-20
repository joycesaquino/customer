package types

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"time"
)

type Customer struct {
	Id        primitive.ObjectID `bson:"_id" json:"id"`
	Name      string             `bson:"name" json:"name" validate:"required"`
	Email     string             `bson:"email" json:"email" validate:"required"`
	CPF       *string            `bson:"cpf" json:"cpf"`
	Image     string             `bson:"image" json:"image"`
	CreatedAt time.Time          `bson:"created_at" json:"created_at"`
	OAuth     Token              `bson:"o_auth" json:"o_auth"`
}

type User struct {
	Name          string `json:"name"`
	Email         string `json:"email"`
	VerifiedEmail bool   `json:"verified_email"`
	Image         string `json:"picture"`
	Token         Token  `json:"token"`
}

type Token struct {
	AccessToken  interface{} `json:"access_token"`
	TokenType    string      `json:"token_type,omitempty"`
	RefreshToken string      `json:"refresh_token,omitempty"`
	Expiry       time.Time   `json:"expiry,omitempty"`
}
