

<h1 align="center"> Compass UOL | Udemy Business </h1>
<p align="center"> DESENVOLVIMENTO 03 </p>

<img src="https://github.com/LukeCesar7/SP_SpringBoot_AWS_Desafio_03_pedido-estoque-client/blob/main/imagens/%23feeecc.png">


### Objetivo

<hr>

 - Este repositório tem como finalidade;

    
    * Desenvolvimento de  um sistema de microserviços em Java utilizando Spring Boot,
     que simula um sistema de gerenciamento de pedidos. O sistema é composto por três microserviços: Pedido, Estoque, e Cliente.
     Esses microserviços se comunicam entre si por meio de chamadas REST e são implementados em instância EC2 da AWS.

### Instrumentalidade

<hr>
    
          * JAVA 17    * DOCKER    * REST    * POSTGRESQL    *SPRINGBOOT JPA    *SWAGGER    *AMAZON WEB SERVICES   *POSTMAN
    
### Visão Geral dos Microserviços

<hr>

**1. Serviço de Pedido:**

- Responsável pela criação, atualização e consulta de pedidos.
- Cada pedido contém um identificador, uma lista de produtos e suas quantidades.
- Verifica a disponibilidade dos produtos no serviço de **Estoque** antes de criar o pedido.
- Atualiza o estoque reduzindo as quantidades quando os produtos são confirmados como disponíveis.
- Caso contrário, retorna uma mensagem de erro informando a falta de estoque.

**2. Serviço de Estoque:**

- Gerencia os produtos disponíveis e suas quantidades.
- Oferece endpoints para consulta e atualização do estoque.
- Permite que o serviço de **Pedido** consulte a disponibilidade de produtos.

**3. Serviço de Cliente:**

- Gerencia o cadastro, atualização e consulta de clientes.
- Fornece um endpoint para que os clientes consultem seu histórico de pedidos. Esses dados são recuperados do serviço de **Pedido**.

### Usabilidade Prática

<hr>

- Baixe o Postman
  * https://www.postman.com/downloads/

- Instale o programa
- Importe o arquivo de teses diponibilizado acima (**MicroServiços.json**)

  -Aproveite o Sistema.
