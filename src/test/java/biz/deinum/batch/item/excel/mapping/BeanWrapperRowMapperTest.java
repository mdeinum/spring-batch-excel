package biz.deinum.batch.item.excel.mapping;

import biz.deinum.batch.item.Player;
import biz.deinum.batch.item.excel.MockSheet;
import biz.deinum.batch.item.excel.support.rowset.DefaultRowSetFactory;
import biz.deinum.batch.item.excel.support.rowset.RowSet;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Marten Deinum
 * @since 0.5.0
 */
public class BeanWrapperRowMapperTest {

    @Test
    public void givenNoNameWhenInitCompleteThenIllegalStateShouldOccur() {
        assertThrows(IllegalStateException.class, () -> {
            BeanWrapperRowMapper<Player> mapper = new BeanWrapperRowMapper<>();
            mapper.afterPropertiesSet();
        });
    }

    @Test
    public void givenAValidRowWhenMappingThenAValidPlayerShouldBeConstructed() throws Exception {
        BeanWrapperRowMapper<Player> mapper = new BeanWrapperRowMapper<>();
        mapper.setTargetType(Player.class);
        mapper.afterPropertiesSet();

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "lastName", "firstName", "position", "birthYear", "debutYear"});
        rows.add( new String[]{"AbduKa00", "Abdul-Jabbar", "Karim", "rb", "1974", "1996"});
        MockSheet sheet = new MockSheet("players", rows);


        RowSet rs = new DefaultRowSetFactory().create(sheet);
        rs.next();
        rs.next();

        Player p = mapper.mapRow(rs);
        assertNotNull(p);
        assertEquals("AbduKa00", p.getId());
        assertEquals("Abdul-Jabbar", p.getLastName());
        assertEquals("Karim", p.getFirstName());
        assertEquals("rb", p.getPosition());
        assertEquals(1974, p.getBirthYear());
        assertEquals(1996, p.getDebutYear());
        assertNull(p.getComment());
    }

    @Test
    public void givenAValidRowWhenMappingThenAValidPlayerShouldBeConstructedBasedOnPrototype() throws Exception {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(TestConfig.class);
        BeanWrapperRowMapper<Player> mapper = ctx.getBean("playerRowMapper", BeanWrapperRowMapper.class);

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "lastName", "firstName", "position", "birthYear", "debutYear"});
        rows.add( new String[]{"AbduKa00", "Abdul-Jabbar", "Karim", "rb", "1974", "1996"});
        MockSheet sheet = new MockSheet("players", rows);

        RowSet rs = new DefaultRowSetFactory().create(sheet);
        rs.next();
        rs.next();
        Player p = mapper.mapRow(rs);

        assertNotNull(p);
        assertEquals("AbduKa00", p.getId());
        assertEquals("Abdul-Jabbar", p.getLastName());
        assertEquals("Karim", p.getFirstName());
        assertEquals("rb", p.getPosition());
        assertEquals(1974, p.getBirthYear());
        assertEquals(1996, p.getDebutYear());
        assertEquals("comment from context", p.getComment());
    }

    @Configuration
    public static class TestConfig {

        @Bean
        public BeanWrapperRowMapper<Player> playerRowMapper() {
            BeanWrapperRowMapper<Player> mapper = new BeanWrapperRowMapper<>();
            mapper.setPrototypeBeanName("player");
            return mapper;
        }

        @Bean
        @Scope(value = "prototype")
        public Player player() {
            Player p = new Player();
            p.setComment("comment from context");
            return p;
        }

    }
}
