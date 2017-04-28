# Spring Cloud Stream Example

Leverages Spring Cloud Stream to process Twitch chat messages

Slides: https://docs.google.com/presentation/d/1tE0gPCW8j1DVzbE1z_4JXvNbW4zOp1MX6rc_4M7Lzys/edit?usp=sharing

## What does it do?

`chatbot` connects to Twitch channel and acts as a `Source` for broadcasting messages, and `Sink` for sending messages

`chatbot-ai` acts as the bot 'brains' for responding to messages

`chattingusers` acts as a `Sink` by listening to messages and aggregating all usernames that have sent a message

`emotewatcher` listens to messages and rebroadcasts all Emotes to another topic.

`twitchviewer` Custom viewer for configured channel that listens to all Emotes and bounces them around randomly. Kappa GG WP
![alt text](./docs/emotes.jpg)

## Running the example

This assumes you have Docker installed. This command will build and run all pieces of the system including RabbitMQ

First: Modify the .env file to contain the proper nick/channel/auth for your bot

``` bash
$ ./mvnw clean package
$ docker-compose build
$ docker-compose up
```
