import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { 
  Home, Users, DoorOpen, Calendar, UtensilsCrossed, 
  ChefHat, Package, PartyPopper, BarChart3, LogOut 
} from 'lucide-react';
import './Sidebar.css';

const Sidebar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const menuItems = [
    { path: '/', icon: Home, label: 'Dashboard' },
    { path: '/employees', icon: Users, label: 'Employees' },
    { path: '/rooms', icon: DoorOpen, label: 'Rooms' },
    { path: '/reservations', icon: Calendar, label: 'Reservations' },
    { path: '/restaurant', icon: UtensilsCrossed, label: 'Restaurant' },
    { path: '/kitchen', icon: ChefHat, label: 'Kitchen' },
    { path: '/inventory', icon: Package, label: 'Inventory' },
    { path: '/events', icon: PartyPopper, label: 'Events' },
    { path: '/statistics', icon: BarChart3, label: 'Statistics' },
  ];

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <div className="logo-container">
          <img src="/logo.png" alt="Hotel Logo" className="hotel-logo" />
        </div>
        <h2>ආලකමන්දා</h2>
        <p>Hotel Management System</p>
      </div>

      <nav className="sidebar-nav">
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => 
                isActive ? 'nav-item active' : 'nav-item'
              }
              end={item.path === '/'}
            >
              <Icon size={20} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </nav>

      {user && (
        <div className="sidebar-footer">
          <div className="user-info">
            <div className="user-avatar">
              {user.fullName?.charAt(0) || user.username?.charAt(0) || 'U'}
            </div>
            <div className="user-details">
              <p className="user-name">{user.fullName || user.username}</p>
              <p className="user-role">{user.role}</p>
            </div>
          </div>
          <button onClick={handleLogout} className="logout-btn">
            <LogOut size={18} />
            <span>Logout</span>
          </button>
        </div>
      )}
    </aside>
  );
};

export default Sidebar;
