package simpleshopapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StartPageController {
    
    @GetMapping("/")
    public String startPage() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Start</title>
                <style>
                    body {
                        display: flex;
                        flex-direction: column;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                        font-family: Arial, sans-serif;
                    }
                    h1 {
                        text-align: center;
                        margin: 10px;
                    }
                    p {
                        text-align: center;
                        margin: 10px;
                    }
                </style>
            </head>
            <body>
                <h1>Datenbanken: Weiterführende Konzepte</h1>
                <p>WISE25/26</p>
                <h2>Willkommen!</h2>
                <p>Sollten Sie die Anwendung lokal laufen haben, so verwenden Sie bitte \n
                <a href="http://localhost:8080/swagger-ui/index.html">Swagger-UI</a>
                oder laden Sie <a href=""https://www.postman.com/>Postman</a> runter für das Testen \n
                der Endpointschnittstellen.</p>
                <p>Greifen Sie die Seite über den Railway-Link zu, dann werden Sie nicht in der Lage sein, \n
                die Anwendung zu testen, da die Anwendung <strong>kein</strong> HTTPS unterstützen tut!
                <p>Quellcode: <a href="https://github.com/Marvin0109/Simple-Shop-API">GitHub Repository</a></p>
            </body>
            </html>
           """;
    }
}
