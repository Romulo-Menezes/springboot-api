<div align="center">

# Product Api

[![build](https://github.com/Romulo-Menezes/springboot-api/actions/workflows/build.yaml/badge.svg)](https://github.com/Romulo-Menezes/springboot-api/actions/workflows/build.yaml)


</div>

> API inspirada neste tutorial: [Spring Boot 3 | Curso Completo 2023](https://youtu.be/wlYvA2b1BWI)

## Rotas

| Descrição                    | Method | URL                               |                 Body                  |
|:-----------------------------|:------:|:----------------------------------|:-------------------------------------:|
| Lista todos os produtos      |  GET   | /products<br/>   /products?page=0 |                                       |
| Exibie um produto específico |  GET   | /products/:id                     |                                       |
| Cria um produto              |  POST  | /products                         | { "name": "string", "value": number } |
| Atualiza um produto          |  PUT   | /products/:id                     | { "name": "string", "value": number } |
| Deleta um produto            | DELETE | /products/:id                     |                                       |