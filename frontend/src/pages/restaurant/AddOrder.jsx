import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { useMutation } from '@tanstack/react-query';
import { createOrder } from '../../services/api';

const AddOrder = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    tableNumber: '',
    roomNumber: '',
    guestName: '',
    orderType: 'DINE_IN',
    guestCount: 1,
    serverName: '',
    specialInstructions: '',
    subtotal: 0,
    taxAmount: 0,
    serviceCharge: 0,
    discountAmount: 0
  });

  const mutation = useMutation({
    mutationFn: createOrder,
    onSuccess: () => {
      alert('Order created successfully!');
      navigate('/restaurant');
    },
    onError: (error) => {
      alert('Failed to create order: ' + (error.response?.data?.message || error.message));
    }
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    const totalAmount = formData.subtotal + formData.taxAmount + formData.serviceCharge;
    const finalAmount = totalAmount - formData.discountAmount;
    
    mutation.mutate({
      ...formData,
      orderNumber: `ORD-${Date.now()}`,
      status: 'PENDING',
      paymentStatus: 'UNPAID',
      totalAmount,
      finalAmount
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => {
      const updated = { ...prev, [name]: value };
      
      // Calculate tax and service charge automatically
      if (name === 'subtotal') {
        const subtotal = parseFloat(value) || 0;
        updated.taxAmount = subtotal * 0.10; // 10% tax
        updated.serviceCharge = subtotal * 0.05; // 5% service charge
      }
      
      return updated;
    });
  };

  return (
    <div>
      <Link to="/restaurant" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      <div className="card">
        <h2>Create New Restaurant Order</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1.5rem' }}>
            <div>
              <label>Order Type *</label>
              <select name="orderType" value={formData.orderType} onChange={handleChange} required>
                <option value="DINE_IN">Dine In</option>
                <option value="ROOM_SERVICE">Room Service</option>
                <option value="TAKEAWAY">Takeaway</option>
              </select>
            </div>
            <div>
              <label>Guest Name *</label>
              <input type="text" name="guestName" value={formData.guestName} onChange={handleChange} required />
            </div>
            
            {formData.orderType === 'DINE_IN' && (
              <div>
                <label>Table Number *</label>
                <input type="number" name="tableNumber" value={formData.tableNumber} onChange={handleChange} min="1" required />
              </div>
            )}
            
            {formData.orderType === 'ROOM_SERVICE' && (
              <div>
                <label>Room Number *</label>
                <input type="number" name="roomNumber" value={formData.roomNumber} onChange={handleChange} min="101" required />
              </div>
            )}
            
            <div>
              <label>Number of Guests *</label>
              <input type="number" name="guestCount" value={formData.guestCount} onChange={handleChange} min="1" required />
            </div>
            <div>
              <label>Server Name</label>
              <input type="text" name="serverName" value={formData.serverName} onChange={handleChange} placeholder="e.g., Hashini" />
            </div>
            <div>
              <label>Subtotal (LKR) *</label>
              <input type="number" name="subtotal" value={formData.subtotal} onChange={handleChange} step="0.01" min="0" required />
            </div>
            <div>
              <label>Tax (10%)</label>
              <input type="number" name="taxAmount" value={formData.taxAmount.toFixed(2)} readOnly />
            </div>
            <div>
              <label>Service Charge (5%)</label>
              <input type="number" name="serviceCharge" value={formData.serviceCharge.toFixed(2)} readOnly />
            </div>
            <div>
              <label>Discount (LKR)</label>
              <input type="number" name="discountAmount" value={formData.discountAmount} onChange={handleChange} step="0.01" min="0" />
            </div>
          </div>
          
          <div style={{ marginTop: '1.5rem' }}>
            <label>Special Instructions</label>
            <textarea name="specialInstructions" value={formData.specialInstructions} onChange={handleChange} rows="3" placeholder="e.g., Extra spicy, no onions" />
          </div>
          
          <div style={{ marginTop: '1.5rem', padding: '1rem', background: '#f0f9ff', borderRadius: '8px' }}>
            <p><strong>Total Amount: LKR {(formData.subtotal + formData.taxAmount + formData.serviceCharge).toFixed(2)}</strong></p>
            <p><strong>Final Amount: LKR {(formData.subtotal + formData.taxAmount + formData.serviceCharge - formData.discountAmount).toFixed(2)}</strong></p>
          </div>
          
          <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem' }}>
            <button type="submit" className="btn btn-primary" disabled={mutation.isPending}>
              {mutation.isPending ? 'Creating...' : 'Create Order'}
            </button>
            <Link to="/restaurant" className="btn btn-secondary">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddOrder;
