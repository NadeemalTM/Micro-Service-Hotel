import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import EmployeeList from './pages/employees/EmployeeList';
import EmployeeDetails from './pages/employees/EmployeeDetails';
import AddEmployee from './pages/employees/AddEmployee';
import EditEmployee from './pages/employees/EditEmployee';
import RoomList from './pages/rooms/RoomList';
import RoomDetails from './pages/rooms/RoomDetails';
import AddRoom from './pages/rooms/AddRoom';
import ReservationList from './pages/reservations/ReservationList';
import AddReservation from './pages/reservations/AddReservation';
import RestaurantOrders from './pages/restaurant/RestaurantOrders';
import AddOrder from './pages/restaurant/AddOrder';
import KitchenOrders from './pages/kitchen/KitchenOrders';
import InventoryList from './pages/inventory/InventoryList';
import AddInventoryItem from './pages/inventory/AddInventoryItem';
import EventList from './pages/events/EventList';
import AddEvent from './pages/events/AddEvent';
import Statistics from './pages/Statistics';
import './App.css';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            <Route index element={<Dashboard />} />
            
            {/* Employee Management */}
            <Route path="employees" element={<EmployeeList />} />
            <Route path="employees/:id" element={<EmployeeDetails />} />
            <Route path="employees/add" element={<AddEmployee />} />
            <Route path="employees/edit/:id" element={<EditEmployee />} />
            
            {/* Room Management */}
            <Route path="rooms" element={<RoomList />} />
            <Route path="rooms/:id" element={<RoomDetails />} />
            <Route path="rooms/add" element={<AddRoom />} />
            
            {/* Reservation Management */}
            <Route path="reservations" element={<ReservationList />} />
            <Route path="reservations/add" element={<AddReservation />} />
            
            {/* Restaurant Management */}
            <Route path="restaurant" element={<RestaurantOrders />} />
            <Route path="restaurant/add" element={<AddOrder />} />
            
            {/* Kitchen Management */}
            <Route path="kitchen" element={<KitchenOrders />} />
            
            {/* Inventory Management */}
            <Route path="inventory" element={<InventoryList />} />
            <Route path="inventory/add" element={<AddInventoryItem />} />
            
            {/* Event Management */}
            <Route path="events" element={<EventList />} />
            <Route path="events/add" element={<AddEvent />} />
            
            {/* Statistics */}
            <Route path="statistics" element={<Statistics />} />
          </Route>
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
