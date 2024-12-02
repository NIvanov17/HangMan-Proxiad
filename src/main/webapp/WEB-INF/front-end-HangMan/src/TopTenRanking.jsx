import { useEffect, useState } from "react";


const TopTenRankings = () => {

    const [playerData, setPlayerData] = useState([]);

    useEffect(() => {
        fetch('http://localhost:8080/api/v1/players/ranking/top-ten')
            .then(res => res.json())
            .then((data) => {
                console.log(data);
                setPlayerData(data);
            })
    }, [])

    return (
        <div className="ranking">
            <h2>Top 10 Players</h2>
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

export default TopTenRankings;