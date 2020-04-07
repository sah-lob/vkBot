package ru.sahlob.core.observers;

import lombok.*;
import org.springframework.context.annotation.Lazy;
import ru.sahlob.core.observers.roles.Roles;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Lazy(false)
public class Observer {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @NonNull private String name;
    @NonNull private String alternativeName;
    private long countOfRequests = 1;
    @NonNull @ElementCollection(fetch = FetchType.EAGER) private Set<Roles> roles;
    @ElementCollection(fetch = FetchType.EAGER) private Map<String, Integer> requests = new HashMap<>();
    @ElementCollection(fetch = FetchType.EAGER) private Set<String> personsId = new HashSet<>();

    public void incrementCountOfRequests() {
        countOfRequests++;
    }

    public void addRequest(String request) {
        request = request.toLowerCase();
        if (requests.containsKey(request)) {
            int count = requests.get(request) + 1;
            requests.put(request, count);
        } else {
            requests.put(request, 1);
        }
    }


}
