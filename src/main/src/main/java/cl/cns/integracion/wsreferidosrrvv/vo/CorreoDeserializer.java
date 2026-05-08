package cl.cns.integracion.wsreferidosrrvv.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class CorreoDeserializer extends StdDeserializer<EnvioCorreoOutput> {

    public CorreoDeserializer() {
        this(null);
    }

    public CorreoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public EnvioCorreoOutput deserialize(
            JsonParser jp,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String codigo = node.get("codigo").asText();
        String mensaje = node.get("mensaje").asText();

        EnvioCorreoOutput output = new EnvioCorreoOutput();
        output.setCodigoCorreo(codigo);
        output.setMensajeCorreo(mensaje);
        return output;
    }
}
