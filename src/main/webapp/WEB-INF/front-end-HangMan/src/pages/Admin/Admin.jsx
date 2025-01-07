import { useEffect, useState } from "react";
import PaginationComponent from "../../Components/Pagination/PaginationComponent";
import "./Admin.css";
import { type } from "@testing-library/user-event/dist/type";

const Admin = () => {
    const [playerData, setPlayerData] = useState([]);
    const [availableRoles] = useState(['USER', 'ADMIN']);
    const [currentSelection, setCurrentSelection] = useState({});
    const [currentPage, setCurrentPage] = useState(0);
    const [itemsPerPage] = useState(5);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/admin/users?page=${currentPage}&size=${itemsPerPage}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
        })
            .then((res) => res.json())
            .then((data) => {
                console.log(data);
                setTotalPages(data.totalPages);
                setPlayerData(data.content);
            })
            .catch((error) => console.error('Error fetching data:', error));
    }, [currentPage]);

    const handleSelectChange = (username, role) => {
        setCurrentSelection({ username, role });
    };

    const addRole = () => {
        if (!currentSelection.role) {
            alert("Please select a role to add!");
            return;
        }

        const { username, role } = currentSelection;

        const roleDTO = {
            username,
            role,
        };

        fetch(`http://localhost:8080/api/v1/admin/users/roles`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
            body: JSON.stringify(roleDTO),
        })
            .then((res) => {
                if (res.ok) {
                    alert("Role added successfully!");
                    setPlayerData((prevData) =>
                        prevData.map((p) =>
                            p.username === username ? { ...p, role: [...p.role, role] } : p
                        )
                    );
                    setCurrentSelection({ username: null, role: "" });
                } else {
                    return res.json().then((err) => {
                        throw new Error(err.message || "Failed to add role");
                    });
                }
            })
            .catch((error) => {
                alert(`Error: ${error.message}`);
            });
    };

    const removeRole = () => {
        if (!currentSelection.role) {
            alert("Please select a role!");
            return;
        }

        const { username, role } = currentSelection;

        const roleDTO = {
            username,
            role,
        }

        fetch(`http://localhost:8080/api/v1/admin/users/roles`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
            body: JSON.stringify(roleDTO),
        })
            .then((res) => {
                if (res.ok) {
                    alert("Role removed successfully!");
                    setPlayerData((prevData) =>
                        prevData.map((p) =>
                            p.username === username ? { ...p, role: p.role.filter((r) => r !== role), } : p));
                    setCurrentSelection({ username: null, role: "" })
                } else {
                    return res.json().then((err) => {
                        throw new Error(err.message || "Failed to add role")
                    });
                }
            }).catch((error) => {
                alert(`Error: ${error.message}`);
            })

    };


    return (
        <div className="admin">
            <h1>Welcome, Admin! {sessionStorage.getItem('username')}</h1>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Roles</th>
                        <th>Add Role</th>
                        <th>Remove Role</th>
                    </tr>
                </thead>
                <tbody>
                    {playerData.map((player) => (
                        <tr key={player.id}>
                            <td>{player.username}</td>
                            <td>{player.role?.join(", ") || "No roles"}</td>
                            <td>
                                <select
                                    onChange={(e) =>
                                        handleSelectChange(player.username, e.target.value)
                                    }
                                >
                                    <option value="" disabled selected>
                                        Select Role
                                    </option>
                                    {availableRoles
                                        .filter((role) => !player.role.includes(role))
                                        .map((role) => (
                                            <option key={role} value={role}>
                                                {role}
                                            </option>
                                        ))}
                                </select>
                                <button onClick={addRole}>Add Role</button>
                            </td>
                            <td>
                                <select
                                    onChange={(e) =>
                                        handleSelectChange(player.username, e.target.value)
                                    }
                                >
                                    <option value="" disabled selected>
                                        Select Role
                                    </option>
                                    {player.role?.map((role) => (
                                        <option key={role} value={role}>
                                            {role}
                                        </option>
                                    ))}
                                </select>
                                <button onClick={removeRole}>Remove Role</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <PaginationComponent
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </div>
    );
};

export default Admin;
