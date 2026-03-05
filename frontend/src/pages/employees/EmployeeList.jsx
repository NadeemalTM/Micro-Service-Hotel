import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { Plus, Edit, Eye, Search } from 'lucide-react';
import { getAllEmployees } from '../../services/api';
import { useState } from 'react';

const EmployeeList = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [departmentFilter, setDepartmentFilter] = useState('ALL');
  
  const { data, isLoading, error } = useQuery({
    queryKey: ['employees'],
    queryFn: async () => {
      const response = await getAllEmployees();
      return response.data.data;
    },
  });

  if (isLoading) return <div className="spinner"></div>;
  if (error) return <div className="error-message">Error loading employees</div>;

  const filteredEmployees = data?.filter(emp => {
    const matchesSearch = emp.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         emp.lastName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         emp.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         emp.position?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDept = departmentFilter === 'ALL' || emp.department === departmentFilter;
    return matchesSearch && matchesDept;
  });

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Employees</h1>
        <Link to="/employees/add" className="btn btn-primary">
          <Plus size={20} /> Add Employee
        </Link>
      </div>

      {/* Search and Filter Bar */}
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem' }}>
        <div className="search-box" style={{ flex: 1 }}>
          <Search size={20} color="#6b7280" />
          <input
            type="text"
            placeholder="Search by name, email, or position..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <select
          value={departmentFilter}
          onChange={(e) => setDepartmentFilter(e.target.value)}
          style={{ padding: '0.75rem', borderRadius: '8px', border: '1px solid #d1d5db', minWidth: '180px' }}
        >
          <option value="ALL">All Departments</option>
          <option value="FRONT_DESK">Front Desk</option>
          <option value="HOUSEKEEPING">Housekeeping</option>
          <option value="KITCHEN">Kitchen</option>
          <option value="RESTAURANT">Restaurant</option>
          <option value="MANAGEMENT">Management</option>
          <option value="MAINTENANCE">Maintenance</option>
        </select>
      </div>

      <div className="table-container">
        <div style={{ marginBottom: '1rem', padding: '1rem', color: '#6b7280', fontSize: '0.875rem', background: 'white' }}>
          Showing {filteredEmployees?.length || 0} of {data?.length || 0} employees
        </div>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Department</th>
              <th>Position</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredEmployees?.map((employee) => (
              <tr key={employee.id}>
                <td>{employee.id}</td>
                <td>{employee.firstName} {employee.lastName}</td>
                <td>{employee.email}</td>
                <td>{employee.department}</td>
                <td>{employee.position}</td>
                <td>
                  <span className={`badge badge-${employee.status === 'ACTIVE' ? 'success' : 'warning'}`}>
                    {employee.status}
                  </span>
                </td>
                <td>
                  <div style={{ display: 'flex', gap: '0.5rem' }}>
                    <Link to={`/employees/${employee.id}`} className="btn btn-secondary" style={{ padding: '0.5rem' }}>
                      <Eye size={16} />
                    </Link>
                    <Link to={`/employees/edit/${employee.id}`} className="btn btn-primary" style={{ padding: '0.5rem' }}>
                      <Edit size={16} />
                    </Link>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default EmployeeList;
