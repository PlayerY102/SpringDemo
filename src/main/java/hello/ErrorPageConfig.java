//package hello;
//
//import org.springframework.boot.web.server.ConfigurableWebServerFactory;
//import org.springframework.boot.web.server.ErrorPage;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//
//@Configuration
//public class ErrorPageConfig {
//    @Bean
//
//    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
//        return (container -> {
//            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/404.html");
//            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
//            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/404.html");
//            container.addErrorPages(error401Page, error404Page, error500Page);
//        });
//    }
//}