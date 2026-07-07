import { API_URL } from "../config/api";
import { getToken } from "../utils/localStorage";

export interface DetallePedidoCreate {
  idProducto: number;
  cantidad: number;
}

export interface PedidoCreate {
  estado: "PENDIENTE";
  formaPago: "TARJETA" | "TRANSFERENCIA" | "EFECTIVO";
  detallePedido: DetallePedidoCreate[];
}

export interface ProductoPedido {
  id: number;
  nombre: string;
  precio: number;
}

export interface DetallePedido {
  id: number;
  cantidad: number;
  subtotal: number;
  producto: ProductoPedido;
}

export interface Pedido {
  id: number;
  fecha: string;
  estado: string;
  total: number;
  formaPago: string;
  idUsuario: number;
  detalles: DetallePedido[];
}

export interface PedidoEdit {
  estado?: string;
  formaPago?: string;
}

const getHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export async function createOrder(data: PedidoCreate): Promise<void> {
  const response = await fetch(`${API_URL}/pedidos`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const body = await response.json();
    throw new Error(body.message ?? "Error al crear el pedido");
  }
}

export async function getMyOrders(): Promise<Pedido[]> {
  const response = await fetch(`${API_URL}/pedidos/mis-pedidos`, {
    headers: getHeaders(),
  });

  if (!response.ok) {
    throw new Error("Error al obtener pedidos");
  }

  return response.json();
}

export async function getAllOrders(): Promise<Pedido[]> {
  const response = await fetch(`${API_URL}/pedidos`, {
    headers: getHeaders(),
  });

  if (!response.ok) {
    throw new Error("Error al obtener pedidos");
  }

  return response.json();
}

export async function updateOrder(
  id: number,
  data: PedidoEdit
): Promise<Pedido> {
  const response = await fetch(`${API_URL}/pedidos/${id}`, {
    method: "PUT",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const body = await response.json();
    throw new Error(body.message ?? "Error al actualizar pedido");
  }

  return response.json();
}

export async function deleteOrder(id: number): Promise<void> {
  const response = await fetch(`${API_URL}/pedidos/${id}`, {
    method: "DELETE",
    headers: getHeaders(),
  });

  if (!response.ok) {
    const body = await response.json();
    throw new Error(body.message ?? "Error al eliminar pedido");
  }
}