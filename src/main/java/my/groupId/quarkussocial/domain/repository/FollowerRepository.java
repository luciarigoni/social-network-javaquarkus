package my.groupId.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import my.groupId.quarkussocial.domain.model.Follower;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
}
