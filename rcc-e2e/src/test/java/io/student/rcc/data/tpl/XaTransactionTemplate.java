package io.student.rcc.data.tpl;

import com.atomikos.icatch.jta.UserTransactionImp;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class XaTransactionTemplate {

  private final JdbcConnectionHolders holders;
  private final AtomicBoolean closedAfterAction = new AtomicBoolean(true);

  public XaTransactionTemplate(String... jdbcUrl) {
    this.holders = Connections.holders(jdbcUrl);
  }

  public @Nonnull XaTransactionTemplate holdConnectionAfterAction() {
    this.closedAfterAction.set(false);
    return this;
  }

  @SafeVarargs
  public final @Nullable <T> T execute(Supplier<T>... actions) {
    UserTransaction ut = new UserTransactionImp();
    try {
      ut.begin();
      T result = null;
      for (Supplier<T> action : actions) {
        result = action.get();
      }
      ut.commit();
      return result;
    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION) {
          ut.rollback();
        }
      } catch (Exception ex) {
        e.addSuppressed(ex);
      }
      throw new RuntimeException(e);
    } finally {
      if (closedAfterAction.get()) {
        holders.close();
      }
    }
  }
}
