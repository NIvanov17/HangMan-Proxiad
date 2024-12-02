import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";



const Ranking = () => {

    const [playerData, setPlayerData] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/api/v1/players/ranking')
            .then(res => res.json())
            .then(data => {
                console.log(data);
                setPlayerData(data)
            });
    }, [])

    const handleSubmit = () => {
        navigate('/rankings/top-ten');
    }

    return (
        <div className="ranking">
            <form onSubmit={handleSubmit}>
                <button>Load Top 10 Players</button>
            </form>
            <h2>All Time Ranking</h2>
            <table>
                <thead>
                    <tr>
                        <th>Player ID</th>
                        <th>Username</th>
                        <th>Total Wins</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        playerData.map((player) => (
                            <tr key={player.id}>
                                <td>{player.id}</td>
                                <td>{player.username}</td>
                                <td>{player.totalWins}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        </div>
    );

}

export default Ranking;