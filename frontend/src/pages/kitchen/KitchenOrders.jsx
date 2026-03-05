import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Clock, ChefHat, CheckCircle } from 'lucide-react';
import { getAllKitchenOrders, updateKitchenOrderStatus } from '../../services/api';

const KitchenOrders = () => {
  const queryClient = useQueryClient();
  
  const { data: orders, isLoading } = useQuery({
    queryKey: ['kitchen-orders'],
    queryFn: async () => {
      const response = await getAllKitchenOrders();
      return response.data.data;
    },
  });

  const updateStatusMutation = useMutation({
    mutationFn: ({ id, status }) => updateKitchenOrderStatus(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['kitchen-orders'] });
      alert('Order status updated successfully!');
    },
    onError: (error) => {
      alert('Failed to update status: ' + (error.response?.data?.message || error.message));
    }
  });

  const handleStatusUpdate = (orderId, newStatus) => {
    updateStatusMutation.mutate({ id: orderId, status: newStatus });
  };

  const getStatusBadge = (status) => {
    const statusColors = {
      PENDING: 'background: #fef3c7; color: #92400e',
      PREPARING: 'background: #dbeafe; color: #1e40af',
      READY: 'background: #d1fae5; color: #065f46',
      COMPLETED: 'background: #e5e7eb; color: #374151',
      CANCELLED: 'background: #fee2e2; color: #991b1b'
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', ...statusColors[status] || {} }}>
        {status}
      </span>
    );
  };

  const pendingOrders = orders?.filter(o => o.status === 'PENDING') || [];
  const preparingOrders = orders?.filter(o => o.status === 'PREPARING') || [];
  const readyOrders = orders?.filter(o => o.status === 'READY') || [];

  return (
    <div>
      <h1>Kitchen Orders</h1>
      
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginBottom: '2rem' }}>
        <div className="card" style={{ textAlign: 'center', background: '#fef3c7' }}>
          <Clock size={32} style={{ margin: '0 auto', color: '#92400e' }} />
          <h3 style={{ margin: '0.5rem 0', color: '#92400e' }}>{pendingOrders.length}</h3>
          <p style={{ color: '#92400e' }}>Pending Orders</p>
        </div>
        <div className="card" style={{ textAlign: 'center', background: '#dbeafe' }}>
          <ChefHat size={32} style={{ margin: '0 auto', color: '#1e40af' }} />
          <h3 style={{ margin: '0.5rem 0', color: '#1e40af' }}>{preparingOrders.length}</h3>
          <p style={{ color: '#1e40af' }}>Preparing</p>
        </div>
        <div className="card" style={{ textAlign: 'center', background: '#d1fae5' }}>
          <CheckCircle size={32} style={{ margin: '0 auto', color: '#065f46' }} />
          <h3 style={{ margin: '0.5rem 0', color: '#065f46' }}>{readyOrders.length}</h3>
          <p style={{ color: '#065f46' }}>Ready</p>
        </div>
      </div>

      <div className="card">
        {isLoading ? (
          <div className="spinner"></div>
        ) : orders && orders.length > 0 ? (
          <div style={{ overflowX: 'auto' }}>
            <table>
              <thead>
                <tr>
                  <th>Order #</th>
                  <th>Table/Room</th>
                  <th>Items</th>
                  <th>Chef</th>
                  <th>Special Instructions</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(order => (
                  <tr key={order.id}>
                    <td><strong>{order.orderNumber || order.id}</strong></td>
                    <td>{order.tableNumber ? `Table ${order.tableNumber}` : order.roomNumber ? `Room ${order.roomNumber}` : 'N/A'}</td>
                    <td>{order.items || order.orderDetails || 'N/A'}</td>
                    <td>{order.assignedChef || order.chefName || 'Unassigned'}</td>
                    <td>{order.specialInstructions || '-'}</td>
                    <td>{getStatusBadge(order.status)}</td>
                    <td>
                      <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                        {order.status === 'PENDING' && (
                          <button 
                            onClick={() => handleStatusUpdate(order.id, 'PREPARING')}
                            className="btn btn-primary"
                            style={{ fontSize: '0.75rem', padding: '4px 8px' }}
                            disabled={updateStatusMutation.isPending}
                          >
                            Start
                          </button>
                        )}
                        {order.status === 'PREPARING' && (
                          <button 
                            onClick={() => handleStatusUpdate(order.id, 'READY')}
                            className="btn btn-primary"
                            style={{ fontSize: '0.75rem', padding: '4px 8px' }}
                            disabled={updateStatusMutation.isPending}
                          >
                            Mark Ready
                          </button>
                        )}
                        {order.status === 'READY' && (
                          <button 
                            onClick={() => handleStatusUpdate(order.id, 'COMPLETED')}
                            className="btn btn-primary"
                            style={{ fontSize: '0.75rem', padding: '4px 8px' }}
                            disabled={updateStatusMutation.isPending}
                          >
                            Complete
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-secondary">No kitchen orders found.</p>
        )}
      </div>
    </div>
  );
};

export default KitchenOrders;
