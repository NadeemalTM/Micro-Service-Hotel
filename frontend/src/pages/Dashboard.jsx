import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  Users, DoorOpen, Calendar, UtensilsCrossed, 
  ChefHat, Package, PartyPopper, TrendingUp 
} from 'lucide-react';
import { 
  getEmployeeStatistics, 
  getRoomStatistics, 
  getReservationStatistics,
  getRestaurantStatistics,
  getKitchenStatistics,
  getInventoryStatistics,
  getEventStatistics
} from '../services/api';
import './Dashboard.css';

const Dashboard = () => {
  const [stats, setStats] = useState({
    employees: null,
    rooms: null,
    reservations: null,
    restaurant: null,
    kitchen: null,
    inventory: null,
    events: null,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStatistics = async () => {
      try {
        const [empStats, roomStats, resStats, restStats, kitchenStats, invStats, eventStats] = await Promise.allSettled([
          getEmployeeStatistics(),
          getRoomStatistics(),
          getReservationStatistics(),
          getRestaurantStatistics(),
          getKitchenStatistics(),
          getInventoryStatistics(),
          getEventStatistics()
        ]);

        setStats({
          employees: empStats.status === 'fulfilled' ? empStats.value.data.data : null,
          rooms: roomStats.status === 'fulfilled' ? roomStats.value.data.data : null,
          reservations: resStats.status === 'fulfilled' ? resStats.value.data.data : null,
          restaurant: restStats.status === 'fulfilled' ? restStats.value.data.data : null,
          kitchen: kitchenStats.status === 'fulfilled' ? kitchenStats.value.data.data : null,
          inventory: invStats.status === 'fulfilled' ? invStats.value.data.data : null,
          events: eventStats.status === 'fulfilled' ? eventStats.value.data.data : null,
        });
      } catch (error) {
        console.error('Error fetching statistics:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStatistics();
  }, []);

  const dashboardCards = [
    {
      title: 'Employees',
      value: stats.employees?.totalEmployees || 0,
      subtitle: `${stats.employees?.activeEmployees || 0} Active`,
      icon: Users,
      color: '#3b82f6',
      link: '/employees',
    },
    {
      title: 'Rooms',
      value: stats.rooms?.totalRooms || 0,
      subtitle: `${stats.rooms?.availableRooms || 0} Available`,
      icon: DoorOpen,
      color: '#10b981',
      link: '/rooms',
    },
    {
      title: 'Reservations',
      value: stats.reservations?.totalReservations || 0,
      subtitle: `${stats.reservations?.confirmedReservations || stats.reservations?.activeReservations || 0} Active`,
      icon: Calendar,
      color: '#f59e0b',
      link: '/reservations',
    },
    {
      title: 'Restaurant',
      value: stats.restaurant?.activeOrders || stats.restaurant?.totalOrders || 0,
      subtitle: `LKR ${(stats.restaurant?.todaysRevenue || 0).toLocaleString()}`,
      icon: UtensilsCrossed,
      color: '#ef4444',
      link: '/restaurant',
    },
    {
      title: 'Kitchen',
      value: stats.kitchen?.pendingOrders || 0,
      subtitle: `${stats.kitchen?.preparingOrders || 0} Preparing`,
      icon: ChefHat,
      color: '#8b5cf6',
      link: '/kitchen',
    },
    {
      title: 'Inventory',
      value: stats.inventory?.totalItems || 0,
      subtitle: `${stats.inventory?.lowStockCount || 0} Low Stock`,
      icon: Package,
      color: '#06b6d4',
      link: '/inventory',
    },
    {
      title: 'Events',
      value: stats.events?.totalEvents || 0,
      subtitle: `${stats.events?.upcomingEvents || 0} Upcoming`,
      icon: PartyPopper,
      color: '#ec4899',
      link: '/events',
    },
    {
      title: 'Revenue',
      value: `LKR ${((stats.reservations?.totalRevenue || 0) / 1000).toFixed(1)}K`,
      subtitle: 'Total Bookings',
      icon: TrendingUp,
      color: '#14b8a6',
      link: '/statistics',
    },
  ];

  if (loading) {
    return <div className="spinner"></div>;
  }

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Dashboard</h1>
        <p>Welcome to ආලකමන්දා Hotel Management System</p>
      </div>

      <div className="dashboard-grid">
        {dashboardCards.map((card, index) => {
          const Icon = card.icon;
          return (
            <Link to={card.link} key={index} className="dashboard-card">
              <div className="card-icon" style={{ backgroundColor: `${card.color}15` }}>
                <Icon size={32} color={card.color} />
              </div>
              <div className="card-content">
                <h3>{card.title}</h3>
                <p className="card-value">{card.value}</p>
                <p className="card-subtitle">{card.subtitle}</p>
              </div>
            </Link>
          );
        })}
      </div>

      <div className="dashboard-insights">
        <div className="insight-card">
          <h3>Occupancy Rate</h3>
          <div className="progress-bar">
            <div 
              className="progress-fill" 
              style={{ width: `${stats.rooms?.occupancyRate || 0}%` }}
            ></div>
          </div>
          <p>{stats.rooms?.occupancyRate?.toFixed(1) || 0}% Occupied ({stats.rooms?.occupiedRooms || 0}/{stats.rooms?.totalRooms || 0} rooms)</p>
        </div>

        <div className="insight-card">
          <h3>Quick Actions</h3>
          <div className="quick-actions">
            <Link to="/reservations/add" className="btn btn-primary">New Reservation</Link>
            <Link to="/restaurant/add" className="btn btn-secondary">New Order</Link>
            <Link to="/employees/add" className="btn btn-primary">Add Employee</Link>
            <Link to="/events/add" className="btn btn-secondary">Create Event</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
