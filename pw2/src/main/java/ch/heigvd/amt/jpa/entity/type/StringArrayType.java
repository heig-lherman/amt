package ch.heigvd.amt.jpa.entity.type;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Represents the mapping between a PostgreSQL array of text and a Java array of strings.
 */
public class StringArrayType implements UserType<String[]> {

    @Override
    public int getSqlType() {
        return Types.ARRAY;
    }

    @Override
    public Class<String[]> returnedClass() {
        return String[].class;
    }

    @Override
    public boolean equals(String[] x, String[] y) {
        return Arrays.equals(x, y);
    }

    @Override
    public int hashCode(String[] x) {
        return Arrays.hashCode(x);
    }

    @Override
    public String[] nullSafeGet(
            ResultSet rs,
            int position,
            SharedSessionContractImplementor session,
            Object owner
    ) throws SQLException {
        Array array = rs.getArray(position);
        if (array == null) {
            return null;
        }

        Object[] arrayObjects = (Object[]) array.getArray();
        return Arrays.copyOf(arrayObjects, arrayObjects.length, String[].class);
    }

    @Override
    public void nullSafeSet(
            PreparedStatement st,
            String[] value,
            int index,
            SharedSessionContractImplementor session
    ) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.ARRAY);
            return;
        }

        try (var conn = session.getJdbcConnectionAccess().obtainConnection()) {
            Array array = conn.createArrayOf("text", value);
            st.setArray(index, array);
        }
    }

    @Override
    public String[] deepCopy(String[] value) {
        return value == null ? null : Arrays.copyOf(value, value.length);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(String[] value) {
        return deepCopy(value);
    }

    @Override
    public String[] assemble(Serializable cached, Object owner) {
        return deepCopy((String[]) cached);
    }
}
