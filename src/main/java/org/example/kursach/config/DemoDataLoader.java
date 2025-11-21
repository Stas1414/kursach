package org.example.kursach.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kursach.domain.DeliveryType;
import org.example.kursach.domain.MasterDataDomain;
import org.example.kursach.domain.MasterDataRecord;
import org.example.kursach.domain.RecordStatus;
import org.example.kursach.domain.SystemUser;
import org.example.kursach.domain.UserRole;
import org.example.kursach.repository.IntegrationChannelRepository;
import org.example.kursach.repository.MasterDataDomainRepository;
import org.example.kursach.repository.MasterDataRecordRepository;
import org.example.kursach.repository.SystemUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.Map;

@Configuration
public class DemoDataLoader {

    @Bean
    CommandLineRunner initData(MasterDataDomainRepository domainRepository,
                               MasterDataRecordRepository recordRepository,
                               IntegrationChannelRepository channelRepository,
                               SystemUserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               ObjectMapper objectMapper) {
        return args -> {
            if (domainRepository.count() == 0) {
                MasterDataDomain products = new MasterDataDomain();
                products.setCode("PRODUCT");
                products.setName("Управление продуктами");
                products.setDescription("Эталонный каталог товаров");
                products.setSteward("product.owner@corp.local");
                domainRepository.save(products);

                MasterDataDomain customers = new MasterDataDomain();
                customers.setCode("CUSTOMER");
                customers.setName("Управление клиентами");
                customers.setDescription("Справочник клиентов B2B");
                customers.setSteward("customer.owner@corp.local");
                domainRepository.save(customers);
            }

            if (recordRepository.count() == 0) {
                for (MasterDataDomain domain : domainRepository.findAll()) {
                    MasterDataRecord record = createRecord(domain, "TST-%s-001".formatted(domain.getCode()), objectMapper);
                    recordRepository.save(record);
                }
            }

            if (channelRepository.count() == 0) {
                var channel = new org.example.kursach.domain.IntegrationChannel();
                channel.setCode("ERP");
                channel.setSystemName("Корпоративная ERP");
                channel.setEndpointUrl("https://erp.corp.local/api/mdm");
                channel.setDeliveryType(DeliveryType.PUSH);
                channelRepository.save(channel);
            }

            if (!userRepository.existsByUsername("admin")) {
                SystemUser admin = new SystemUser();
                admin.setUsername("admin");
                admin.setFullName("Администратор системы");
                admin.setEmail("admin@corp.local");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRoles(EnumSet.of(UserRole.ADMIN, UserRole.DATA_STEWARD));
                userRepository.save(admin);
            }
        };
    }

    private MasterDataRecord createRecord(MasterDataDomain domain,
                                          String key,
                                          ObjectMapper mapper) {
        MasterDataRecord record = new MasterDataRecord();
        record.setDomain(domain);
        record.setBusinessKey(key);
        try {
            record.setPayloadJson(mapper.writeValueAsString(Map.of(
                    "code", domain.getCode() + "-SKU",
                    "name", "Demo entity " + domain.getCode(),
                    "description", "Сгенерировано автоматически"
            )));
        } catch (Exception e) {
            record.setPayloadJson("{}");
        }
        record.setOwner(domain.getSteward());
        record.setStatus(RecordStatus.DRAFT);
        return record;
    }
}

