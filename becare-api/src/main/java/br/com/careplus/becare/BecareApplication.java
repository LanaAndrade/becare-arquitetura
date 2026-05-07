package br.com.careplus.becare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BeCare API – Ponto de entrada da aplicação.
 *
 * <p>Módulo social e gamificado integrado ao app Care Plus.
 * Os beneficiários publicam microações saudáveis, acumulam insígnias
 * e participam de desafios colaborativos alinhados aos 7 pilares de bem-estar.</p>
 *
 * @author Caio Freitas, Caio Hideki, Jorge Booz, Lana Andrade, Mateus Tibão
 * @version 1.0.0 – Sprint 3 (SOA e WebServices)
 */
@SpringBootApplication
public class BecareApplication {

    public static void main(String[] args) {
        SpringApplication.run(BecareApplication.class, args);
    }
}
