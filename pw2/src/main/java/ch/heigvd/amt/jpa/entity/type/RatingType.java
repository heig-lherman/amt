package ch.heigvd.amt.jpa.entity.type;

import ch.heigvd.amt.jpa.entity.enums.Rating;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

/**
 * Represents the mapping between the mpaa_rating type the corresponding Java enum.
 */
public class RatingType implements UserType<Rating> {

    @Override
    public int getSqlType() {
        return SqlTypes.ENUM;
    }

    @Override
    public Class<Rating> returnedClass() {
        return Rating.class;
    }

    @Override
    public boolean equals(Rating x, Rating y) {
        return x == y;
    }

    @Override
    public int hashCode(Rating x) {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    public Rating nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String name = rs.getString(position);
        if (name != null) {
            return Rating.fromCode(name);
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Rating value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, SqlTypes.ENUM);
        } else {
            PGobject pgObject = new PGobject();
            pgObject.setType("mpaa_rating");
            pgObject.setValue(value.getCode());
            st.setObject(index, pgObject);
        }
    }

    @Override
    public Rating deepCopy(Rating value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Rating value) {
        // Serialize as the enum name
        return value != null ? value.name() : null;
    }

    @Override
    public Rating assemble(Serializable cached, Object o) {
        // Restore enum from name
        return cached != null ? Rating.fromCode((String) cached) : null;
    }

    @Override
    public Rating replace(Rating original, Rating target, Object owner) {
        // Return original as Enums are immutable
        return original;
    }
}
