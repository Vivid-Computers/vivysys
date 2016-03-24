package vivyclient.print.reports;

import java.math.BigDecimal;
import java.util.Calendar;

public class GSTTotals {
   private Calendar periodStart;
   private Calendar periodEnd;
   private BigDecimal gstSaleTotals;
   private BigDecimal gstExemptSaleTotals;

   public Calendar getPeriodStart() {
      return this.periodStart;
   }

   public void setPeriodStart(Calendar periodStart) {
      this.periodStart = periodStart;
   }

   public Calendar getPeriodEnd() {
      return this.periodEnd;
   }

   public void setPeriodEnd(Calendar periodEnd) {
      this.periodEnd = periodEnd;
   }

   public BigDecimal getGstSaleTotals() {
      return this.gstSaleTotals;
   }

   public void setGstSaleTotals(BigDecimal gstSaleTotals) {
      this.gstSaleTotals = gstSaleTotals;
   }

   public BigDecimal getGstExemptSaleTotals() {
      return this.gstExemptSaleTotals;
   }

   public void setGstExemptSaleTotals(BigDecimal gstExemptSaleTotals) {
      this.gstExemptSaleTotals = gstExemptSaleTotals;
   }
}
