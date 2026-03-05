import axios from 'axios';

// Base API configuration
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost';

// Service URLs
const EMPLOYEE_SERVICE = `${API_BASE_URL}:8085/api`;
const ROOM_SERVICE = `${API_BASE_URL}:8086/api`;
const RESERVATION_SERVICE = `${API_BASE_URL}:8087/api`;
const RESTAURANT_SERVICE = `${API_BASE_URL}:8088/api`;
const KITCHEN_SERVICE = `${API_BASE_URL}:8089/api`;
const INVENTORY_SERVICE = `${API_BASE_URL}:8090/api`;
const EVENT_SERVICE = `${API_BASE_URL}:8091/api`;

// Create axios instances for each service
const createServiceClient = (baseURL) => {
  const client = axios.create({ baseURL });
  
  // Request interceptor - Add auth token
  client.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );
  
  // Response interceptor - Handle errors
  client.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
      return Promise.reject(error);
    }
  );
  
  return client;
};

// Service clients
const employeeClient = createServiceClient(EMPLOYEE_SERVICE);
const roomClient = createServiceClient(ROOM_SERVICE);
const reservationClient = createServiceClient(RESERVATION_SERVICE);
const restaurantClient = createServiceClient(RESTAURANT_SERVICE);
const kitchenClient = createServiceClient(KITCHEN_SERVICE);
const inventoryClient = createServiceClient(INVENTORY_SERVICE);
const eventClient = createServiceClient(EVENT_SERVICE);

// ==================== Authentication API ====================
export const login = (credentials) => 
  employeeClient.post('/auth/login', credentials);

export const validateToken = (token) => 
  employeeClient.post('/auth/validate', { token });

export const changePassword = (data) => 
  employeeClient.post('/auth/change-password', data);

export const getCurrentUser = () => 
  employeeClient.get('/auth/me');

// ==================== Employee API ====================
export const getAllEmployees = () => 
  employeeClient.get('/employees');

export const getEmployeeById = (id) => 
  employeeClient.get(`/employees/${id}`);

export const createEmployee = (data) => 
  employeeClient.post('/employees', data);

export const updateEmployee = (id, data) => 
  employeeClient.put(`/employees/${id}`, data);

export const deleteEmployee = (id) => 
  employeeClient.delete(`/employees/${id}`);

export const searchEmployees = (name) => 
  employeeClient.get(`/employees/search?name=${name}`);

export const getEmployeesByDepartment = (department) => 
  employeeClient.get(`/employees/department/${department}`);

export const getEmployeeStatistics = () => 
  employeeClient.get('/employees/statistics');

// ==================== Room API ====================
export const getAllRooms = () => 
  roomClient.get('/rooms');

export const getRoomById = (id) => 
  roomClient.get(`/rooms/${id}`);

export const getRoomByNumber = (roomNumber) => 
  roomClient.get(`/rooms/number/${roomNumber}`);

export const createRoom = (data) => 
  roomClient.post('/rooms', data);

export const updateRoom = (id, data) => 
  roomClient.put(`/rooms/${id}`, data);

export const deleteRoom = (id) => 
  roomClient.delete(`/rooms/${id}`);

export const getRoomsByFloor = (floor) => 
  roomClient.get(`/rooms/floor/${floor}`);

export const getRoomsByType = (type) => 
  roomClient.get(`/rooms/type/${type}`);

export const getRoomsByStatus = (status) => 
  roomClient.get(`/rooms/status/${status}`);

export const getAvailableRoomsByType = (type) => 
  roomClient.get(`/rooms/available/${type}`);

export const updateRoomStatus = (id, status) => 
  roomClient.patch(`/rooms/${id}/status?status=${status}`);

export const markRoomCleaned = (id) => 
  roomClient.patch(`/rooms/${id}/clean`);

export const getRoomStatistics = () => 
  roomClient.get('/rooms/statistics');

// ==================== Reservation API ====================
export const getAllReservations = () => reservationClient.get('/reservations');

export const getReservationById = (id) => 
  reservationClient.get(`/reservations/${id}`);

export const createReservation = (data) => 
  reservationClient.post('/reservations', data);

export const updateReservation = (id, data) => 
  reservationClient.put(`/reservations/${id}`, data);

export const deleteReservation = (id) => 
  reservationClient.delete(`/reservations/${id}`);

export const getReservationsByStatus = (status) => 
  reservationClient.get(`/reservations/status/${status}`);

export const checkIn = (id) => 
  reservationClient.patch(`/reservations/${id}/checkin`);

export const checkOut = (id) => 
  reservationClient.patch(`/reservations/${id}/checkout`);

export const cancelReservation = (id) => 
  reservationClient.patch(`/reservations/${id}/cancel`);

export const getReservationStatistics = () => 
  reservationClient.get('/reservations/statistics');

// ==================== Restaurant API ====================
export const getAllOrders = () => 
  restaurantClient.get('/orders');

export const getOrderById = (id) => 
  restaurantClient.get(`/orders/${id}`);

export const createOrder = (data) => 
  restaurantClient.post('/orders', data);

export const updateOrderStatus = (id, status) => 
  restaurantClient.patch(`/orders/${id}/status?status=${status}`);

export const getOrdersByStatus = (status) => 
  restaurantClient.get(`/orders/status/${status}`);

export const getRestaurantStatistics = () => 
  restaurantClient.get('/orders/statistics');

// ==================== Kitchen API ====================
export const getAllKitchenOrders = () => 
  kitchenClient.get('/kitchen-orders');

export const getKitchenOrderById = (id) => 
  kitchenClient.get(`/kitchen-orders/${id}`);

export const updateKitchenOrderStatus = (id, status) => 
  kitchenClient.patch(`/kitchen-orders/${id}/status?status=${status}`);

export const getKitchenOrdersByStatus = (status) => 
  kitchenClient.get(`/kitchen-orders/status/${status}`);

export const getKitchenStatistics = () => 
  kitchenClient.get('/kitchen-orders/statistics');

// ==================== Inventory API ====================
export const getAllInventoryItems = () => 
  inventoryClient.get('/inventory');

export const getInventoryItemById = (id) => 
  inventoryClient.get(`/inventory/${id}`);

export const createInventoryItem = (data) => 
  inventoryClient.post('/inventory', data);

export const updateInventoryItem = (id, data) => 
  inventoryClient.put(`/inventory/${id}`, data);

export const deleteInventoryItem = (id) => 
  inventoryClient.delete(`/inventory/${id}`);

export const getLowStockItems = () => 
  inventoryClient.get('/inventory/low-stock');

export const getInventoryStatistics = () => 
  inventoryClient.get('/inventory/statistics');

// ==================== Event API ====================
export const getAllEvents = () => 
  eventClient.get('/events');

export const getEventById = (id) => 
  eventClient.get(`/events/${id}`);

export const createEvent = (data) => 
  eventClient.post('/events', data);

export const updateEvent = (id, data) => 
  eventClient.put(`/events/${id}`, data);

export const deleteEvent = (id) => 
  eventClient.delete(`/events/${id}`);

export const getEventsByStatus = (status) => 
  eventClient.get(`/events/status/${status}`);

export const getUpcomingEvents = () => 
  eventClient.get('/events/upcoming');

export const getEventStatistics = () => 
  eventClient.get('/events/statistics');

export default {
  // Auth
  login,
  validateToken,
  changePassword,
  getCurrentUser,
  
  // Employees
  getAllEmployees,
  getEmployeeById,
  createEmployee,
  updateEmployee,
  deleteEmployee,
  searchEmployees,
  getEmployeesByDepartment,
  getEmployeeStatistics,
  
  // Rooms
  getAllRooms,
  getRoomById,
  getRoomByNumber,
  createRoom,
  updateRoom,
  deleteRoom,
  getRoomsByFloor,
  getRoomsByType,
  getRoomsByStatus,
  getAvailableRoomsByType,
  updateRoomStatus,
  markRoomCleaned,
  getRoomStatistics,
  
  // Reservations
  getAllReservations,
  getReservationById,
  createReservation,
  updateReservation,
  deleteReservation,
  getReservationsByStatus,
  checkIn,
  checkOut,
  cancelReservation,
  getReservationStatistics,
  
  // Restaurant
  getAllOrders,
  getOrderById,
  createOrder,
  updateOrderStatus,
  getOrdersByStatus,
  getRestaurantStatistics,
  
  // Kitchen
  getAllKitchenOrders,
  getKitchenOrderById,
  updateKitchenOrderStatus,
  getKitchenOrdersByStatus,
  getKitchenStatistics,
  
  // Inventory
  getAllInventoryItems,
  getInventoryItemById,
  createInventoryItem,
  updateInventoryItem,
  deleteInventoryItem,
  getLowStockItems,
  getInventoryStatistics,
  
  // Events
  getAllEvents,
  getEventById,
  createEvent,
  updateEvent,
  deleteEvent,
  getEventsByStatus,
  getUpcomingEvents,
  getEventStatistics,
};
