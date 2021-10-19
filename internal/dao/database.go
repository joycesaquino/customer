package dao

import (
	"context"
	"github.com/caarlos0/env"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"time"
)

type Config struct {
	User               string        `env:"DATABASE_USER,required"`
	Password           string        `env:"DATABASE_PASSWORD,required"`
	Uri                string        `env:"DATABASE_URI,required"`
	DatabaseName       string        `env:"DATABASE_NAME,required"`
	DatabaseCollection string        `env:"DATABASE_COLLECTION,required"`
	ConnectionTimeout  time.Duration `env:"DATABASE_TIMEOUT,required"`
}

type CustomerDatabase struct {
	Collection *mongo.Collection
}

func connection(ctx context.Context, client *mongo.Client) error {
	err := client.Connect(ctx)
	if err != nil {
		return err
	}
	err = client.Ping(ctx, nil)
	if err != nil {
		return err
	}

	return nil
}

func CustomerDao(ctx context.Context) (error, *CustomerDatabase) {
	var config Config
	err := env.Parse(&config)
	if err != nil {
		log.Fatalf("Error to configure Customer Database cliente. Error : %s", err)
	}

	dbOptions := options.Client()
	dbOptions.ApplyURI(config.Uri)
	dbOptions.SetConnectTimeout(config.ConnectionTimeout)

	client, err := mongo.NewClient(dbOptions)
	if err != nil {
		return err, nil
	}

	if err := connection(ctx, client); err != nil {
		log.Fatalf("Error to configure Customer Database cliente. Error : %s", err)
	}

	return nil, &CustomerDatabase{
		Collection: client.Database(config.DatabaseName).Collection(config.DatabaseCollection),
	}

}
