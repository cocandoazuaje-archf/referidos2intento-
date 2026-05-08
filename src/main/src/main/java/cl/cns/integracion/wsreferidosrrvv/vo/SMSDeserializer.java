package cl.cns.integracion.wsreferidosrrvv.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class SMSDeserializer extends StdDeserializer<SMSOutput> {

    public SMSDeserializer() {
        this(null);
    }

    public SMSDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SMSOutput deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        int codigo = node.get("codigo").intValue();
        String mensaje = node.get("mensaje").asText();

        SMSOutput output = new SMSOutput();
        output.setCodigo(codigo);
        output.setMensaje(mensaje);
        return output;
    }
}
