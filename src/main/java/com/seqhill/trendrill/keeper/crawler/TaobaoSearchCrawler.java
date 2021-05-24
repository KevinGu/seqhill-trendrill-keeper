package com.seqhill.trendrill.keeper.crawler;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.mime.Header;
import org.apache.hc.client5.http.entity.mime.MimeField;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author kevin
 */
public class TaobaoSearchCrawler {
    public static Logger logger = LoggerFactory.getLogger(TaobaoSearchCrawler.class);

    public static String URL_FORMAT = "https://h5api.m.taobao.com/h5/mtop.taobao.tvtao.tvtaosearchservice.getbyq2/1.0/?jsv=2.6.0&appKey=12574478&t=%s&sign=%s&api=mtop.tmall.inshopsearch.searchitems&v=1.0&type=json&dataType=jsonp&data=%s";
    public static String COOKIE_M_H5_TK = "";
    public static String COOKIE_M_H5_TK_ENC = "";

    private final static String HTTP_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36";

    public static void refreshToken() throws IOException {
        String url = String.format(URL_FORMAT, System.currentTimeMillis(), "", "");

        HttpClient instance = HttpClientBuilder.create().setUserAgent(HTTP_AGENT).build();
        HttpGet request = new HttpGet(url);
        HttpClientContext context = new HttpClientContext();
        context.setAttribute(HttpClientContext.COOKIE_STORE, new BasicCookieStore());
        HttpResponse response = instance.execute(request, context);
        if (response.getCode() == 200) {
            List<Cookie> cookies = context.getCookieStore().getCookies();
            for (Cookie cookie : cookies) {
                if ("_m_h5_tk".equalsIgnoreCase(cookie.getName())) {
                    COOKIE_M_H5_TK = cookie.getValue();
                    continue;
                }
                if ("_m_h5_tk_enc".equalsIgnoreCase(cookie.getName())) {
                    COOKIE_M_H5_TK_ENC = cookie.getValue();
                }
            }
            logger.info("update token:[" + COOKIE_M_H5_TK + "][" + COOKIE_M_H5_TK_ENC + "]");
        } else {
            logger.error("fail to update token,return http code is " + response.getCode());
        }
    }

    public static void main(String[] args) throws IOException {
        TaobaoSearchCrawler.refreshToken();
    }
}
