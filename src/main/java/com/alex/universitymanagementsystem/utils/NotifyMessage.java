package com.alex.universitymanagementsystem.utils;

/**
 * Creare una classe di rappresentazione delle risorse
 *
 * Ora che hai configurato il progetto e il sistema di build,
 * puoi creare il tuo servizio di messaggistica STOMP.
 * Inizia il processo pensando alle interazioni del servizio.
 * Il servizio accetterà i messaggi che contengono un nome in un
 * messaggio STOMP il cui corpo è un oggetto JSON.
 *  {
 *      "name": "Fred"
 *  }
 *
 * Per modellare il messaggio che trasporta il nome, puoi creare
 * un semplice oggetto Java con una proprietà name e un metodo getName()
 * corrispondente, come mostra il seguente elenco
 *
 * Dopo aver ricevuto il messaggio ed estratto il nome, il servizio lo
 * elaborerà creando un saluto e pubblicandolo su una coda separata a cui
 * il client è iscritto.
 * Il saluto sarà anche un oggetto JSON, come mostrato nell'elenco seguente:
 *
 *  {
 *     "content": "Hello, Fred!"
 *  }
 */

public class NotifyMessage {

    // instance variables
    private String message;

    // constructors
    public NotifyMessage(String message) {
        this.message = message;
    }

    // getters and setters
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
