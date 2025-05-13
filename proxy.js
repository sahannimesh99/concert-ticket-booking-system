const http = require('http');
const net = require('net');

const PORT = 8080;
const NODE_PORTS = [9001, 9002, 9003];

function getRandomNodePort() {
    return NODE_PORTS[Math.floor(Math.random() * NODE_PORTS.length)];
}

const server = http.createServer((req, res) => {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, DELETE, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

    if (req.method === 'OPTIONS') {
        res.writeHead(204);
        res.end();
        return;
    }

    const handleSocketRequest = (rpcMessage) => {
        const backendPort = getRandomNodePort();
        const socket = new net.Socket();
        socket.connect(backendPort, '0.0.0.0');
        socket.on('connect', () => {
            console.log(`Sending to node ${backendPort}: ${rpcMessage}`);
            socket.write(rpcMessage + '\n');
        });
        socket.on('data', data => {
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end(data.toString());
            socket.end();
        });
        socket.on('error', (err) => {
            console.error("Proxy connection error:", err.message);
            res.writeHead(500);
            res.end('Backend node unreachable');
        });
    };

    if (req.method === 'POST' && req.url === '/add') {
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => handleSocketRequest(body));

    } else if (req.method === 'POST' && req.url === '/reserve') {
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => handleSocketRequest(`RESERVE_JSON|${body}`));

    } else if (req.method === 'POST' && req.url === '/bulk-reserve') {
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => handleSocketRequest(`BULK_RESERVE_JSON|${body}`));

    } else if (req.method === 'GET' && req.url === '/concerts') {
        const backendPort = getRandomNodePort();
        const socket = new net.Socket();
        socket.connect(backendPort, '0.0.0.0');
        socket.on('connect', () => {
            socket.write('GET_CONCERTS\n');
        });
        socket.on('data', data => {
            try {
                const json = JSON.parse(data.toString());
                res.writeHead(200, { 'Content-Type': 'application/json' });
                res.end(JSON.stringify(json));
            } catch (err) {
                res.writeHead(500);
                res.end('Invalid JSON returned from backend');
            }
            socket.end();
        });
        socket.on('error', () => {
            res.writeHead(500);
            res.end('RPC Node not reachable');
        });

    } else if (req.method === 'POST' && req.url === '/customer/add') {
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => handleSocketRequest(`CUSTOMER_ADD_JSON|${body}`));

    } else if (req.method === 'POST' && req.url.startsWith('/customer/update/')) {
        const id = req.url.split('/').pop();
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => handleSocketRequest(`CUSTOMER_UPDATE_JSON|${id}|${body}`));

    } else if (req.method === 'DELETE' && req.url.startsWith('/customer/delete/')) {
        const id = req.url.split('/').pop();
        handleSocketRequest(`CUSTOMER_DELETE|${id}`);

    } else if (req.method === 'GET' && req.url === '/customer/all') {
        const backendPort = getRandomNodePort();
        const socket = new net.Socket();
        socket.connect(backendPort, '0.0.0.0');
        socket.on('connect', () => {
            socket.write('CUSTOMER_ALL\n');
        });
        socket.on('data', data => {
            try {
                const json = JSON.parse(data.toString());
                res.writeHead(200, { 'Content-Type': 'application/json' });
                res.end(JSON.stringify(json));
            } catch (err) {
                res.writeHead(500);
                res.end('Invalid JSON returned from backend');
            }
            socket.end();
        });
        socket.on('error', () => {
            res.writeHead(500);
            res.end('RPC Node not reachable');
        });

    } else {
        res.writeHead(404);
        res.end('Not Found');
    }
});

server.listen(PORT, () => {
    console.log(`Proxy running at http://localhost:${PORT}`);
});
