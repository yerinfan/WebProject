package kr.ac.kopo.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

@Service
public class NaverSearchService {

	@Value("${naver.client-id}")
	private String clientId;
	@Value("${naver.client-secret}")
	private String clientSecret;
	private final RestTemplate rest = new RestTemplate();

	/**
	 * query를 받아서 - URL 인코딩 - blog.json 호출 - items 배열 꺼내 title/description HTML 태그
	 * 제거 - URL 디코딩(%EC%A7%B8 → 한글) - HTML 엔티티 디코딩(&amp; → &) - Map.of(title, link,
	 * desc) 형태로 리턴
	 */
	public List<Map<String, String>> search(String query) {
		// 1) 쿼리 URL 인코딩
		System.out.println("쿼리 값 : "+query);
		String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
		System.out.println("인코딩 값 :  "+encoded);
		// 2) 원하는 엔드포인트로 호출 (blog, webkr, news 등)
		String url = "https://openapi.naver.com/v1/search/news.json" + "?query=" + encoded + "&display=5" + "&sort=sim";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		Map<?, ?> raw = rest.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

		// 3) items 꺼내기
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> items = (List<Map<String, Object>>) raw.get("items");

		// 4) 가공 + 디코딩
		return items.stream().map(item -> {
			// 원본에서 <b> 태그 제거
			String t = ((String) item.get("title")).replaceAll("<[^>]+>", "");
			String d = ((String) item.get("description")).replaceAll("<[^>]+>", "");

			// percent-encoding 디코딩
			try {
				t = URLDecoder.decode(t, StandardCharsets.UTF_8);
				d = URLDecoder.decode(d, StandardCharsets.UTF_8);
			} catch (IllegalArgumentException ignored) {
			}

			// HTML 엔티티 디코딩 (&amp;, &#39; 등)
            t = HtmlUtils.htmlUnescape(t);
            d = HtmlUtils.htmlUnescape(d);

			return Map.of("title", t, "link", (String) item.get("link"), "desc", d);
		}).collect(Collectors.toList());
	}
}
