import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { useMutation } from '@tanstack/react-query';
import { createInventoryItem } from '../../services/api';

const AddInventoryItem = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    itemCode: '',
    itemName: '',
    category: 'FOOD',
    description: '',
    quantity: 0,
    unit: 'PIECES',
    reorderLevel: 0,
    maxStockLevel: 0,
    unitPrice: 0,
    supplier: '',
    supplierContact: '',
    location: ''
  });

  const mutation = useMutation({
    mutationFn: createInventoryItem,
    onSuccess: () => {
      alert('Inventory item created successfully!');
      navigate('/inventory');
    },
    onError: (error) => {
      alert('Failed to create inventory item: ' + (error.response?.data?.message || error.message));
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    mutation.mutate(formData);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div>
      <Link to="/inventory" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      <div className="card">
        <h2>Add New Inventory Item</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1.5rem' }}>
            <div>
              <label>Item Code *</label>
              <input type="text" name="itemCode" value={formData.itemCode} onChange={handleChange} placeholder="e.g., FOOD-001" required />
            </div>
            <div>
              <label>Item Name *</label>
              <input type="text" name="itemName" value={formData.itemName} onChange={handleChange} required />
            </div>
            <div>
              <label>Category *</label>
              <select name="category" value={formData.category} onChange={handleChange} required>
                <option value="FOOD">Food</option>
                <option value="BEVERAGE">Beverage</option>
                <option value="CLEANING">Cleaning</option>
                <option value="LINEN">Linen</option>
                <option value="AMENITIES">Amenities</option>
                <option value="MAINTENANCE">Maintenance</option>
                <option value="OTHER">Other</option>
              </select>
            </div>
            <div>
              <label>Unit *</label>
              <select name="unit" value={formData.unit} onChange={handleChange} required>
                <option value="PIECES">Pieces</option>
                <option value="KG">Kilograms</option>
                <option value="LITERS">Liters</option>
                <option value="BOXES">Boxes</option>
                <option value="PACKS">Packs</option>
                <option value="BOTTLES">Bottles</option>
                <option value="PAIRS">Pairs</option>
              </select>
            </div>
            <div>
              <label>Current Quantity *</label>
              <input type="number" name="quantity" value={formData.quantity} onChange={handleChange} step="0.01" min="0" required />
            </div>
            <div>
              <label>Reorder Level *</label>
              <input type="number" name="reorderLevel" value={formData.reorderLevel} onChange={handleChange} step="0.01" min="0" required />
            </div>
            <div>
              <label>Max Stock Level *</label>
              <input type="number" name="maxStockLevel" value={formData.maxStockLevel} onChange={handleChange} step="0.01" min="0" required />
            </div>
            <div>
              <label>Unit Price (LKR) *</label>
              <input type="number" name="unitPrice" value={formData.unitPrice} onChange={handleChange} step="0.01" min="0" required />
            </div>
            <div>
              <label>Supplier *</label>
              <input type="text" name="supplier" value={formData.supplier} onChange={handleChange} required />
            </div>
            <div>
              <label>Supplier Contact</label>
              <input type="text" name="supplierContact" value={formData.supplierContact} onChange={handleChange} placeholder="+94-11-2345678" />
            </div>
            <div>
              <label>Storage Location</label>
              <input type="text" name="location" value={formData.location} onChange={handleChange} placeholder="e.g., Cold Storage" />
            </div>
          </div>
          
          <div style={{ marginTop: '1.5rem' }}>
            <label>Description</label>
            <textarea name="description" value={formData.description} onChange={handleChange} rows="3" />
          </div>
          
          <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem' }}>
            <button type="submit" className="btn btn-primary" disabled={mutation.isPending}>
              {mutation.isPending ? 'Creating...' : 'Create Inventory Item'}
            </button>
            <Link to="/inventory" className="btn btn-secondary">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddInventoryItem;
