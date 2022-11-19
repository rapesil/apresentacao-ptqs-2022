# Melhorando a automação de testes de API Rest na prática

Código usado na apresentação da palestra `Melhorando a automação de testes de API Rest na prática` na Conferência de testes de API Rest do Júlio de Lima

## Prerrequisitos

* Java 11
* Docker

## Como rodar esse teste na sua máquina

Você pode rodar local, subindo a aplicação na sua máquina o que te permite rodar alguns testes com o Postman, por exemplo.
Antes é necessário subir o banco:

```
docker-compose up
```

Assim que o banco subir na porta `3306`, basta rodar:

```
./gradlew bootRun
```

A aplicação subirá na porta `8080`. Pronto, já podemos começar a fazer testes. Uma vez que a API estiver de pé, você consegue consultar o swagger em:

```
http://localhost:8080/swagger-ui/#/book-controller
```

Uma outra opção é você rodar os testes automatizados que já existem nesse projeto. Para rodar os testes unitários:

```
./gradlew test
```

Para rodar os testes de integração:

```
./gradlew integrationTest
```

Ao rodar o comando acima, um servidor local subirá numa porta randômica. O teste já está preparado para isso. Ao final da execução o servidor é derrubado 
automaticamente. Nesses testes é utilizado um banco H2 (banco em memória), o banco também é inicializado sozinho e finalizado ao término da execução.
Por ser um banco em memória, os dados inseridos/alterados na base são perdidos a cada nova execução, sempre iniciando em um estado determinado no arquivo
`data.sql`.

Não é foco dessa apresentação, mas ainda é possível a execução de testes de mutação com o seguinte comando:

```
./gradlew pitest
```

O projeto ainda conta com uma pipeline utilizando GitHub Actions que, além de rodar os testes automaticamente a cada novo push/pr,
ainda gera um relatório do Allure Report em uma [github page que você ver aqui](https://rapesil.github.io/apresentacao-ptqs-2022/)

