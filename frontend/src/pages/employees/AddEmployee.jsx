import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
import { createEmployee } from '../../services/api';

const AddEmployee = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: '', lastName: '', email: '', phone: '',
    position: '', department: '', salary: '', hireDate: new Date().toISOString().split('T')[0],
    status: 'ACTIVE', address: '', username: '', password: 'password123', role: 'EMPLOYEE'
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createEmployee(formData);
      alert('Employee created successfully!');
      navigate('/employees');
    } catch (error) {
      alert('Error creating employee');
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div>
      <Link to="/employees" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      
      <div className="card">
        <h2>Add New Employee</h2>
        <form onSubmit={handleSubmit} style={{ marginTop: '2rem' }}>
          <div className="grid grid-2">
            <div className="form-group">
              <label className="form-label">First Name</label>
              <input className="form-input" name="firstName" value={formData.firstName} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Last Name</label>
              <input className="form-input" name="lastName" value={formData.lastName} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input type="email" className="form-input" name="email" value={formData.email} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Phone</label>
              <input className="form-input" name="phone" value={formData.phone} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Department</label>
              <select className="form-select" name="department" value={formData.department} onChange={handleChange} required>
                <option value="">Select Department</option>
                <option value="FRONT_DESK">Front Desk</option>
                <option value="HOUSEKEEPING">Housekeeping</option>
                <option value="KITCHEN">Kitchen</option>
                <option value="RESTAURANT">Restaurant</option>
                <option value="MANAGEMENT">Management</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Position</label>
              <input className="form-input" name="position" value={formData.position} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Salary</label>
              <input type="number" className="form-input" name="salary" value={formData.salary} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label className="form-label">Username</label>
              <input className="form-input" name="username" value={formData.username} onChange={handleChange} required />
            </div>
          </div>
          <button type="submit" className="btn btn-primary">Create Employee</button>
        </form>
      </div>
    </div>
  );
};

export default AddEmployee;
