package it.vitalegi.cocorido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.vitalegi.cocorido.util.LogExecutionTime;
import it.vitalegi.cocorido.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest")
public class SystemStatusController {

	@Autowired
	List<CrudRepository<?, ?>> repos;

	@LogExecutionTime
	@GetMapping("/repos")
	public String logRepos() {
		StringBuffer sb = new StringBuffer();
		repos.forEach(repo -> {
			List<?> values = SqlUtil.convert(repo.findAll());
			sb.append("Repo: " + values.size() + "<br>");
			values.forEach(value -> sb.append("- " + value + "<br>"));
		});
		return sb.toString();
	}
}
