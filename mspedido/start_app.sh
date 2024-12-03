#!/bin/bash

IP_PUBLICO=$(curl http://169.254.169.254/latest/meta-data/public-ipv4)

export ENDPOINT_CLIENTE="http://$IP_PUBLICO:8081/cliente-service/api/clientes"
export ENDPOINT_ESTOQUE="http://$IP_PUBLICO:8082/estoque/api/estoque"

docker-compose up