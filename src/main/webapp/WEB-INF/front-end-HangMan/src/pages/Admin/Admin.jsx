import { useEffect, useState } from "react";

const Admin = () => {

    useEffect(() => {
        fetch('http://localhost:8080/api/v1/admin', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem("token")}`
            }
        }).then(res => res.json())
            .then(data => {
                console.log(data);
            })
    }, []);


    return (
        <div className="admin">
            <h1>Welcome, Admin!{sessionStorage.getItem('username')}</h1>
        </div>
    );
}

export default Admin;