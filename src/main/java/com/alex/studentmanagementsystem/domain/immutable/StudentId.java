package com.alex.studentmanagementsystem.domain.immutable;

import java.util.UUID;
import java.io.Serializable;

import org.springframework.util.Assert;

import jakarta.persistence.Embeddable;

/**
 * I record in Java sono una feature introdotta nella versione 14
 * del linguaggio (rilasciata nel marzo 2020) e migliorata nelle
 * versioni successive.
 * Ecco le peculiarità principali dei record in Java:
 *
 *
 * Definizione
 *
 * Un record è una classe speciale che rappresenta un insieme di
 * valori immutabili.
 * La sua definizione è molto concisa e consiste solo nella lista
 * dei campi e del loro tipo.
 *
 *
 * Esempio
 *
 * public record Persona(String nome, String cognome, int eta) {}
 *
 *
 * Caratteristiche
 *
 * Immutabilità: i record sono immutabili per default, quindi non è
 *               possibile modificare i valori dei campi dopo la
 *               creazione dell'istanza.
 *
 * Definizione concisa: la definizione di un record è molto più concisa
 *                      rispetto a una classe normale, poiché non è
 *                      necessario definire i metodi getter e setter.
 *
 * Metodi automatici: il compilatore Java genera automaticamente i metodi
 *                    toString(), equals() e hashCode() in base ai campi
 *                    del record.
 *
 * Costruttore: il compilatore Java genera anche un costruttore che accetta
 *              come parametri i valori dei campi del record.
 *
 *
 * Vantaggi
 *
 * Maggiore leggibilità: la definizione di un record è molto più concisa e
 *                       facile da leggere rispetto a una classe normale.
 *
 * Maggiore sicurezza: i record sono immutabili, quindi non è possibile
 *                     modificare i valori dei campi in modo accidentale.
 *
 * Maggiore efficienza: il compilatore Java genera automaticamente i metodi
 *                      toString(), equals() e hashCode(), quindi non è
 *                      necessario scrivere codice ridondante.
 *
 * In sintesi, i record in Java sono una feature molto utile per rappresentare
 * insiemi di valori immutabili in modo conciso e sicuro.
 */

@Embeddable
public record StudentId(UUID id) implements Serializable {

    public StudentId {
        Assert.notNull(id, "id must not be null");
    }

    public StudentId() {
        this(UUID.randomUUID());
    }

}
