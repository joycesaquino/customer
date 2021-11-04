package dao

import (
	"context"
	"github.com/caarlos0/env"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"time"
)

const ErrorDatabase = "Error on configure Customer Database client. Error : %s"

type Config struct {
	Uri        string        `env:"DATABASE_URI,required"`
	DbName     string        `env:"DATABASE_NAME,required"`
	Collection string        `env:"DATABASE_COLLECTION,required"`
	Timeout    time.Duration `env:"DATABASE_TIMEOUT,required"`
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
	if err := env.Parse(&config); err != nil {
		log.Fatalf(ErrorDatabase, err)
	}

	dbOptions := options.Client()
	dbOptions.ApplyURI(config.Uri)
	dbOptions.SetConnectTimeout(config.Timeout)

	client, err := mongo.NewClient(dbOptions)
	if err != nil {
		return err, nil
	}

	if err := connection(ctx, client); err != nil {
		log.Fatalf(ErrorDatabase, err)
	}

	return nil, &CustomerDatabase{
		Collection: client.
			Database(config.DbName).
			Collection(config.Collection),
	}

}
