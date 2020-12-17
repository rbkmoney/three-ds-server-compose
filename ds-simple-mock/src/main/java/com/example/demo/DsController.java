package com.example.demo;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DsController {

    private final JsonMapper jsonMapper;

    @PostMapping("/visa/DS2/authenticate")
    @SneakyThrows
    public ResponseEntity<Object> visa(@RequestBody Message message) {
        String pres = "visa/3DSS-210-001/pres.json";
        String ares = "visa/3DSS-210-101/ares.json";
        return ds(message, pres, ares);
    }

    @PostMapping("/mastercard/DS2/authenticate")
    @SneakyThrows
    public ResponseEntity<Object> mastercard(@RequestBody Message message) {
        String pres = "mir/1-8/pres.json";
        String ares = "mir/1-1/ares.json";
        return ds(message, pres, ares);
    }

    private ResponseEntity<Object> ds(@RequestBody Message message, String pres, String ares) {
        log.info("start {}", message);
        if (message instanceof PReq) {
            Message body = readMessage(pres);
            ((PRes) body).setThreeDSServerTransID(message.getThreeDSServerTransID());
            log.info("end {}", body.toString());
            return ResponseEntity.ok(body);
        }
        if (message instanceof AReq) {
            Message body = readMessage(ares);
            ((ARes) body).setThreeDSServerTransID(message.getThreeDSServerTransID());
            log.info("end {}", body.toString());
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.notFound().build();
    }

    private Message readMessage(String fullPath) {
        return jsonMapper.readFromFile(fullPath, Message.class);
    }
}
