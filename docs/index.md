# SALAS - Trabalho Engenharia de Software II

No SALAS, é possivel fazer o agendamento de salas para uso em uma universidade. <br>
O usuário, seja ele docente ou terceirizado, poderá visualizar e realizar agendamentos. <br>
O administrador do sistema pode visualizar, editar e adicionar salas ao sistema.

## Rodando o projeto
A aplicação, base de dados e demais bibliotecas utilizadas estarão dentro de um container docker. <BR>
Para executar o projeto, siga o passo a passo.

 1. Instale o Docker. 
    * [https://docs.docker.com/get-started/get-docker/](https://docs.docker.com/get-started/get-docker/)
    
 2. Baixe o arquivo `docker-compose.yml` que está no repositório.
    * [repositório](https://github.com/CTISM-Prof-Henry/trab-final-spi-integer-s)

 3. Execute o comando com `prompt de comando` aberto no mesmo diretório que o arquivo recém baixado.
    ```bash
    docker-compose up -d
    ```
 4. Acesse o projeto em seu navegador utilizando o link:
    * [https://localhost:8080](https://localhost:8080)


