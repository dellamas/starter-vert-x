package com.dellamas.starter;

// Importações principais do Vert.x
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MainVerticle extends VerticleBase {

  @Override
  public Future<?> start() {
    // Criamos o roteador principal da aplicação.
    // Ele será responsável por interceptar e encaminhar as requisições HTTP para os handlers apropriados.
    Router router = Router.router(vertx);

    // Aqui estamos definindo um handler genérico para qualquer rota, em qualquer método HTTP (GET, POST, etc).
    // Este será chamado sempre que uma requisição for recebida.
    router.route().handler(context -> {
      // Obtemos o endereço remoto de quem fez a requisição (ex: IP do cliente).
      String address = context.request().connection().remoteAddress().toString();

      // Capturamos os parâmetros da query string da requisição (ex: ?name=luis).
      MultiMap queryParams = context.queryParams();

      // Se existir um parâmetro chamado "name", usamos ele. Caso contrário, usamos "desconhecido".
      String name = queryParams.contains("name") ? queryParams.get("name") : "desconhecido";

      // Respondemos à requisição com um JSON contendo nome, endereço e uma mensagem personalizada.
      context.json(
        new JsonObject()
          .put("name", name)
          .put("address", address)
          .put("message", "Olá " + name + " conectado de " + address)
      );
    });

    // Criamos um servidor HTTP na porta 8888.
    // A cada requisição recebida, ela será tratada pelo router definido acima.
    return vertx.createHttpServer()
      .requestHandler(router) // define o roteador como handler principal
      .listen(8888) // inicia o servidor na porta 8888
      .onSuccess(server -> {
        // Se o servidor iniciar com sucesso, mostramos a porta no console.
        System.out.println("HTTP server started on port " + server.actualPort());
      })
      .onFailure(throwable -> {
        // Se algo der errado, mostramos o erro no console.
        throwable.printStackTrace();
      });
  }
}
