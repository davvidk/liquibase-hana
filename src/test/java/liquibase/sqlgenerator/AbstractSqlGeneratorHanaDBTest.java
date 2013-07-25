package liquibase.sqlgenerator;

import liquibase.database.Database;
import liquibase.database.ext.HanaDBDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.CreateTableStatement;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public abstract class AbstractSqlGeneratorHanaDBTest<T extends SqlStatement> {

    protected SqlGenerator<T> generatorUnderTest;

    public AbstractSqlGeneratorHanaDBTest(SqlGenerator<T> generatorUnderTest) throws Exception {
        this.generatorUnderTest = generatorUnderTest;
    }

    protected abstract T createSampleSqlStatement();

    protected void dropAndCreateTable(CreateTableStatement statement, Database database) throws SQLException, DatabaseException {
        ExecutorService.getInstance().getExecutor(database).execute(statement);

        if (!database.getAutoCommitMode()) {
            database.getConnection().commit();
        }

    }

    @Test
    public void isImplementation() throws Exception {
//        for (Database database : TestContextHanaDB.getInstance().getAllDatabases()) {
//            boolean isImpl = generatorUnderTest.supports(createSampleSqlStatement(), database);
//            if (shouldBeImplementation(database)) {
//                assertTrue("Unexpected false supports for " + database.getTypeName(), isImpl);
//            } else {
//                assertFalse("Unexpected true supports for " + database.getTypeName(), isImpl);
//            }
//        }
        Database database = new HanaDBDatabase();
        boolean isImpl = generatorUnderTest.supports(createSampleSqlStatement(), database);
        if (shouldBeImplementation(database)) {
            assertTrue("Unexpected false supports for " + database.getTypeName(), isImpl);
        } else {
            assertFalse("Unexpected true supports for " + database.getTypeName(), isImpl);
        }
    }

    @Test
    public void isValid() throws Exception {
//        for (Database database : TestContextHanaDB.getInstance().getAllDatabases()) {
//        	if (shouldBeImplementation(database)) {
//            	if (waitForException(database)) {
//            		assertTrue("The validation should be failed for " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
//            	} else {
//            		assertFalse("isValid failed against " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
//            	}
//
//        	}
//        }
        Database database = new HanaDBDatabase();
        if (shouldBeImplementation(database)) {
            if (waitForException(database)) {
                assertTrue("The validation should be failed for " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
            } else {
                assertFalse("isValid failed against " + database, generatorUnderTest.validate(createSampleSqlStatement(), database, new MockSqlGeneratorChain()).hasErrors());
            }
        }
    }

    @Test
    public void checkExpectedGenerator() {
        assertEquals(this.getClass().getName().replaceFirst("Test$", ""), generatorUnderTest.getClass().getName());
    }

    protected boolean waitForException(Database database) {
        return false;
    }

    protected boolean shouldBeImplementation(Database database) {
        return true;
    }

}
