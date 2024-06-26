package io.github.li_shenglin.desensitization.core;

import io.github.li_shenglin.desensitization.config.DesensitizationConfig;
import io.github.li_shenglin.desensitization.config.MatchConfig;
import org.apache.commons.lang3.math.NumberUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置读取
 *
 * @author shenglin.li  2024/4/3 21:58
 * @version 1.0
 */
public interface ConfigReader {
    DesensitizationConfig getConfig() throws IOException;

    class DefaultConfigReader implements ConfigReader {
        @Override
        public DesensitizationConfig getConfig() {
            try {
                DesensitizationConfig desensitizationConfig = readConfig("/log-desensitization.yml");
                if (desensitizationConfig != null) {
                    return desensitizationConfig;
                }
                return readConfig("/log-desensitization-default.yml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public DesensitizationConfig readConfig(String path) throws IOException {
            Yaml yaml = new Yaml();
            try (InputStream ins = getClass().getResourceAsStream(path)) {
                if (ins == null) {
                    return null;
                }
                Map<String, Object> load = yaml.loadAs(ins, Map.class);
                Map<String, Object> o = (Map<String, Object>) load.get("log-desensitization");
                DesensitizationConfig config = new DesensitizationConfig();

                config.setBaseLoggerPackage(readStringArr(o.get("base-logger-package")));
                config.setMaxLength(2048);
                if(o.get("max-length") != null && NumberUtils.isCreatable(o.get("max-length").toString())) {
                    config.setMaxLength(Integer.valueOf(o.get("max-length").toString()));
                }

                List matcherList = (List) o.get("matcher");
                if (matcherList != null) {
                    List<MatchConfig> collect = new ArrayList<>();
                    matcherList.forEach(e -> {
                        Map<String, Object> map = (Map<String, Object>) e;
                        MatchConfig matchConfig = new MatchConfig();
                        matchConfig.setKeywords(readStringArr(map.get("keywords")));
                        String[] strings = readStringArr(map.get("end-symbols"));
                        matchConfig.setEndSymbols(String.join("", strings).chars().mapToObj(c -> (char) c).collect(Collectors.toList()).toArray(new Character[0]));
                        matchConfig.setType(map.get("type").toString());
                        matchConfig.setDesensitization(map.get("desensitization").toString());
                        matchConfig.setPattens(readStringArr(map.get("pattens")));


                        matchConfig.setIgnoreCase(map.get("ignore-case") == null || Boolean.parseBoolean(map.get("ignore-case").toString()));
                        matchConfig.setDepth(map.get("depth") == null ? 1 : Integer.parseInt(map.get("depth").toString()));

                        collect.add(matchConfig);
                    });
                    config.setMatcher(collect);
                }
                return config;
            }
        }

        private String[] readStringArr(Object obj) {
            List list = (List) obj;
            if (list != null && !list.isEmpty()) {
                String[] baseLoggerPackage = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    baseLoggerPackage[i] = list.get(i).toString();
                }
                return baseLoggerPackage;
            }
            return new String[0];
        }
    }
}
