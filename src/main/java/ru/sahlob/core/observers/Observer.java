package ru.sahlob.core.observers;

import lombok.*;
import org.springframework.context.annotation.Lazy;
import ru.sahlob.core.observers.roles.Role;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Transient private List<Role> roles;
    @ElementCollection(fetch = FetchType.EAGER) private Map<String, Integer> personsName = new HashMap<>();
    @ElementCollection(fetch = FetchType.EAGER) private Map<String, Integer> requests = new HashMap<>();

    public void incrementCountOfRequests() {
        countOfRequests++;
    }

    public void addPersonsName(String personsName) {
        this.personsName.put(personsName, 0);
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
