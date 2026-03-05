import { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { getEmployeeById, updateEmployee } from '../../services/api';

const EditEmployee = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', phone: '',
    position: '', department: '', salary: '', hireDate: '',
    status: 'ACTIVE', address: ''
  });

  useEffect(() => {
    const fetchEmployee = async () => {
      try {
        const response = await getEmployeeById(id);
        const emp = response.data.data;
        setFormData({
          firstName: emp.firstName || '',
          lastName: emp.lastName || '',
          email: emp.email || '',
          phone: emp.phone || '',
          position: emp.position || '',
          department: emp.department || '',
          salary: emp.salary || '',
          hireDate: emp.hireDate ? emp.hireDate.split('T')[0] : '',
          status: emp.status || 'ACTIVE',
          address: emp.address || ''
        });
        setLoading(false);
      } catch (error) {
        alert('Error loading employee');
        navigate('/employees');
      }
    };
    fetchEmployee();
  }, [id, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateEmployee(id, formData);
      alert('Employee updated successfully!');
      navigate('/employees');
    } catch (error) {
      alert('Error updating employee: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  if (loading) return <div className="spinner"></div>;

  return (
    <div>
      <Link to="/employees" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      
      <div className="card">
        <h2>Edit Employee</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1.5rem' }}>
            <div>
              <label>First Name *</label>
              <input name="firstName" value={formData.firstName} onChange={handleChange} required />
            </div>
            <div>
              <label>Last Name *</label>
              <input name="lastName" value={formData.lastName} onChange={handleChange} required />
            </div>
            <div>
              <label>Email *</label>
              <input type="email" name="email" value={formData.email} onChange={handleChange} required />
            </div>
            <div>
              <label>Phone *</label>
              <input name="phone" value={formData.phone} onChange={handleChange} required />
            </div>
            <div>
              <label>Department *</label>
              <select name="department" value={formData.department} onChange={handleChange} required>
                <option value="">Select Department</option>
                <option value="FRONT_DESK">Front Desk</option>
                <option value="HOUSEKEEPING">Housekeeping</option>
                <option value="KITCHEN">Kitchen</option>
                <option value="RESTAURANT">Restaurant</option>
                <option value="MANAGEMENT">Management</option>
                <option value="MAINTENANCE">Maintenance</option>
              </select>
            </div>
            <div>
              <label>Position *</label>
              <input name="position" value={formData.position} onChange={handleChange} required />
            </div>
            <div>
              <label>Salary (LKR) *</label>
              <input type="number" name="salary" value={formData.salary} onChange={handleChange} required />
            </div>
            <div>
              <label>Status *</label>
              <select name="status" value={formData.status} onChange={handleChange} required>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
                <option value="ON_LEAVE">On Leave</option>
              </select>
            </div>
            <div>
              <label>Hire Date</label>
              <input type="date" name="hireDate" value={formData.hireDate} onChange={handleChange} />
            </div>
            <div>
              <label>Address</label>
              <input name="address" value={formData.address} onChange={handleChange} />
            </div>
          </div>
          <div style={{ marginTop: '2rem', display: 'flex', gap: '1rem' }}>
            <button type="submit" className="btn btn-primary">Update Employee</button>
            <Link to="/employees" className="btn btn-secondary">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditEmployee;
