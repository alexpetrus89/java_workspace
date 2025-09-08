package com.alex.universitymanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alex.universitymanagementsystem.entity.User;
import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.entity.immutable.UserId;

/**
| Caso                                              | Azione consigliata                               |
| ------------------------------------------------- | ------------------------------------------------ |
| Form di ricerca                                   | Ritorna `Optional` o `null`, mostra un messaggio |
| Operazione su entità esistente (modifica/elimina) | Lancia un'eccezione se non trovata               |
| API REST                                          | Usa `404 Not Found` se l’oggetto non esiste      |
| Interfaccia utente (MVC)                          | Mostra pagina “nessun risultato”                 |
 */

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {


    /**
     * Find user by username
     * @param username
     * @return an optional user
     */
    Optional<User> findByUsername(String username);


    /**
     * Find user by fullname
     * @param firstName
     * @param lastName
     * @return List<User>
     */
    @Query(value = "SELECT s FROM User s WHERE s.firstName = ?1 AND s.lastName = ?2")
    List<User> findByFullname(String firstName, String lastName);


    /**
     * Find user by dob
     * @param dob
     * @return List<User>
     */
    List<User> findByDob(LocalDate dob);


    /**
     * Find user by fiscal code
     * @param fiscalCode
     * @return User
     */
    Optional<User> findByFiscalCode_FiscalCode(String fiscalCode);


    /**
     * Check if a user with the given username exists
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsername(String username);


    /**
     * Check if a user with the given fiscal code exists
     * @param fiscalCode the fiscal code to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByFiscalCode_FiscalCode(String fiscalCode);


    /**
     * Check if a user with the given username exists
     * @param username the username to check
     * @param id the id to exclude
     * @return true if the user exists, false otherwise
     */
    boolean existsByUsernameAndIdNot(String username, UserId id);


    /**
     * Check if a user with the given fiscal code exists
     * @param fiscalCode the fiscal code to check
     * @param id the id to exclude
     * @return true if the user exists, false otherwise
     */
    boolean existsByFiscalCodeAndIdNot(FiscalCode fiscalCode, UserId id);


}
