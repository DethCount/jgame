package count.jgame.services;

import org.springframework.stereotype.Service;

@Service
public class SlugService {
	public String get(String input)
	{
		return input
			.toLowerCase()
			.replaceAll("[^a-z0-9-]", "-")
			.replaceAll("-+", "-")
			.replace("^-", "")
			.replace("-$", "");
	}
}
