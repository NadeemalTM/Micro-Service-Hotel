import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { Plus, Edit, Package, AlertTriangle } from 'lucide-react';
import { getAllInventoryItems } from '../../services/api';

const InventoryList = () => {
  const { data: items, isLoading } = useQuery({
    queryKey: ['inventory'],
    queryFn: async () => {
      const response = await getAllInventoryItems();
      return response.data.data;
    },
  });

  const getStockBadge = (item) => {
    const quantity = item.quantity || 0;
    const reorderLevel = item.reorderLevel || 0;
    
    if (quantity === 0) {
      return <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', background: '#fee2e2', color: '#991b1b' }}>OUT OF STOCK</span>;
    } else if (quantity <= reorderLevel) {
      return <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', background: '#fef3c7', color: '#92400e' }}>LOW STOCK</span>;
    } else {
      return <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', background: '#d1fae5', color: '#065f46' }}>IN STOCK</span>;
    }
  };

  const getCategoryBadge = (category) => {
    const colors = {
      FOOD: '#dbeafe',
      BEVERAGE: '#e0e7ff',
      CLEANING: '#fef3c7',
      LINEN: '#e0f2fe',
      AMENITIES: '#f3e8ff',
      MAINTENANCE: '#fee2e2',
      OTHER: '#e5e7eb'
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', background: colors[category] || colors.OTHER }}>
        {category}
      </span>
    );
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Inventory Management</h1>
        <Link to="/inventory/add" className="btn btn-primary">
          <Plus size={20} /> Add Item
        </Link>
      </div>

      {isLoading ? (
        <div className="card"><div className="spinner"></div></div>
      ) : items && items.length > 0 ? (
        <div className="card">
          <div style={{ overflowX: 'auto' }}>
            <table>
              <thead>
                <tr>
                  <th>Item Code</th>
                  <th>Item Name</th>
                  <th>Category</th>
                  <th>Quantity</th>
                  <th>Unit</th>
                  <th>Reorder Level</th>
                  <th>Supplier</th>
                  <th>Stock Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.id} style={{ 
                    background: (item.quantity || 0) <= (item.reorderLevel || 0) ? '#fef3c7' : 'transparent' 
                  }}>
                    <td><strong>{item.itemCode || item.sku || `ITEM-${item.id}`}</strong></td>
                    <td>
                      <Package size={14} style={{ display: 'inline', marginRight: '4px' }} />
                      {item.itemName || item.name}
                    </td>
                    <td>{getCategoryBadge(item.category)}</td>
                    <td>
                      <strong>{item.quantity || 0}</strong>
                      {(item.quantity || 0) <= (item.reorderLevel || 0) && (
                        <AlertTriangle size={14} style={{ display: 'inline', marginLeft: '4px', color: '#f59e0b' }} />
                      )}
                    </td>
                    <td>{item.unit || 'PIECES'}</td>
                    <td>{item.reorderLevel || 0}</td>
                    <td>{item.supplier || 'N/A'}</td>
                    <td>{getStockBadge(item)}</td>
                    <td>
                      <button className="btn btn-secondary" style={{ padding: '0.5rem', fontSize: '0.875rem' }}>
                        <Edit size={16} /> Edit
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
          <p style={{ textAlign: 'center', color: '#64748b' }}>No inventory items found. Add your first item!</p>
        </div>
      )}
    </div>
  );
};

export default InventoryList;
