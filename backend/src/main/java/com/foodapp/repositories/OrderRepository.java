package com.foodapp.repositories;

import com.foodapp.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByUserIdAndStatus(Long userId, Order.Status status);
    @Query("select sum(o.totalPrice) from Order o where o.completionTime between :startDate and :endDate " +
            "and o.status = 'COMPLETED'")
    List<Object[]> findMonthlyRevenue(@Param("startDate")Timestamp startDate,
                                      @Param("endDate")Timestamp endDate);
    @Query("select od.food.foodName,sum(od.quantity) from OrderDetail od join od.order o " +
            "where o.completionTime between :startDate and :endDate " +
            "and o.status = 'COMPLETED' group by od.food.foodName")
    List<Object[]> findFoodDistribution(@Param("startDate")Timestamp startDate,
                                        @Param("endDate")Timestamp endDate);
    @Query("select o.status,count(o.Id) from Order o where o.completionTime between :startDate and :endDate " +
            "group by o.status")
    List<Object[]> findOrderStatusDistribution(@Param("startDate")Timestamp startDate,
                                               @Param("endDate")Timestamp endDate);
    @Query("select sum(o.totalPrice),o.orderPlatform from Order o " +
            "where o.completionTime between :startDate and :endDate " +
            "group by o.orderPlatform")
    List<Object[]> findPlatformRevenue(@Param("startDate")Timestamp startDate,
                                       @Param("endDate")Timestamp endDate);
    @Query("select od.food.category.name,sum(od.quantity) from OrderDetail od join od.order o " +
            "where o.completionTime between :startDate and :endDate " +
            "and o.status = 'COMPLETED' group by od.food.category.name")
    List<Object[]> findCategorySales(@Param("startDate")Timestamp startDate,
                                     @Param("endDate")Timestamp endDate);
    @Query("SELECT DAY(o.completionTime), COUNT(o) " +
            "FROM Order o " +
            "WHERE o.completionTime BETWEEN :startDate AND :endDate " +
            "AND o.status = 'COMPLETED' " +
            "GROUP BY DAY(o.completionTime) " +
            "ORDER BY DAY(o.completionTime)")
    List<Object[]> getDailyVolume(@Param("startDate")Timestamp startDate,
                                  @Param("endDate")Timestamp endDate);
}
