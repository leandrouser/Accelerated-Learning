package com.estudo.demo.security;

import com.estudo.demo.model.People;
import com.estudo.demo.enums.TypePerson;
import com.estudo.demo.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminCpf = "08277765436"; // CPF padrão (pode mudar)
        String adminName = "Leo";
        String adminPassword = "Leo352019"; // senha padrão (recomendado mudar depois)

        // Verifica se já existe o admin
        if (personRepository.findByCpf(adminCpf).isEmpty()) {
            People admin = new People();
            admin.setCpf(adminCpf);
            admin.setName(adminName);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setType(TypePerson.ADMINISTRATOR);
            admin.setActive(true);
            admin.setPhone("81999549958");

            personRepository.save(admin);
            System.out.println("✅ Usuário administrador criado automaticamente!");
            System.out.println("➡️  Login:");
            System.out.println("CPF: " + adminCpf);
            System.out.println("Senha: " + adminPassword);
        } else {
            System.out.println("👑 Usuário administrador já existe.");
        }
    }
}
