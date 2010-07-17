package jogoshannon.client;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

public class DuplicadorCookie extends RpcRequestBuilder {

    @Override
    protected RequestBuilder doCreate(String serviceEntryPoint) {

        String cookieSessao = Cookies.getCookie("JSESSIONID");

        if (cookieSessao != null) {
            serviceEntryPoint = serviceEntryPoint + "?id_sessao="
                    + cookieSessao;
        }

        return super.doCreate(serviceEntryPoint);
    }

}
