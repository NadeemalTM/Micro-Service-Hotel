import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { Plus, Eye, DollarSign } from 'lucide-react';
import { getAllOrders } from '../../services/api';

const RestaurantOrders = () => {
  const { data: orders, isLoading } = useQuery({
    queryKey: ['restaurant-orders'],
    queryFn: async () => {
      const response = await getAllOrders();
      return response.data.data;
    },
  });

  const getStatusBadge = (status) => {
    const colors = {
      PENDING: 'background: #fef3c7; color: #92400e',
      PREPARING: 'background: #dbeafe; color: #1e40af',
      READY: 'background: #d1fae5; color: #065f46',
      SERVED: 'background: #e0e7ff; color: #3730a3',
      COMPLETED: 'background: #e5e7eb; color: #374151',
      CANCELLED: 'background: #fee2e2; color: #991b1b'
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', ...colors[status] }}>
        {status}
      </span>
    );
  };

  const getPaymentBadge = (status) => {
    const colors = {
      PAID: 'background: #d1fae5; color: #065f46',
      UNPAID: 'background: #fee2e2; color: #991b1b',
      PARTIAL: 'background: #fef3c7; color: #92400e'
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', ...colors[status] }}>
        {status}
      </span>
    );
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Restaurant Orders</h1>
        <Link to="/restaurant/add" className="btn btn-primary">
          <Plus size={20} /> Create Order
        </Link>
      </div>

      {isLoading ? (
        <div className="card"><div className="spinner"></div></div>
      ) : orders && orders.length > 0 ? (
        <div className="card">
          <div style={{ overflowX: 'auto' }}>
            <table>
              <thead>
                <tr>
                  <th>Order #</th>
                  <th>Guest Name</th>
                  <th>Type</th>
                  <th>Table/Room</th>
                  <th>Amount (LKR)</th>
                  <th>Payment</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr key={order.id}>
                    <td><strong>{order.orderNumber || `#${order.id}`}</strong></td>
                    <td>{order.guestName || order.customerName || 'N/A'}</td>
                    <td>{order.orderType || 'DINE_IN'}</td>
                    <td>
                      {order.orderType === 'DINE_IN' && order.tableNumber ? `Table ${order.tableNumber}` :
                       order.orderType === 'ROOM_SERVICE' && order.roomNumber ? `Room ${order.roomNumber}` : 'N/A'}
                    </td>
                    <td>
                      <DollarSign size={14} style={{ display: 'inline' }} />
                      {parseFloat(order.finalAmount || order.totalAmount || 0).toLocaleString()}
                    </td>
                    <td>{getPaymentBadge(order.paymentStatus || 'UNPAID')}</td>
                    <td>{getStatusBadge(order.status)}</td>
                    <td>
                      <button className="btn btn-secondary" style={{ padding: '0.5rem', fontSize: '0.875rem' }}>
                        <Eye size={16} /> View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="card">
          <p style={{ textAlign: 'center', color: '#64748b' }}>No orders found. Create your first order!</p>
        </div>
      )}
    </div>
  );
};

export default RestaurantOrders;
