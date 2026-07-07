import { API_URL } from "../config/api";
import { getToken } from "../utils/localStorage";
import type { Product } from "../types/product";

const getHeaders = () => ({
  "Content-Type": "application/json",
  Authorization: `Bearer ${getToken()}`,
});

export interface ProductCreate {
  nombre: string;
  precio: number;
  descripcion: string;
  stock: number;
  imagen: string;
  disponible: boolean;
  idCategoria: number;
}

export async function getProducts(): Promise<Product[]> {
  const response = await fetch(`${API_URL}/productos`, {
    headers: getHeaders(),
  });

  if (!response.ok) {
    throw new Error("Error al obtener productos");
  }

  return response.json();
}

export async function createProduct(data: ProductCreate): Promise<Product> {
  const response = await fetch(`${API_URL}/productos`, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error("Error al crear producto");
  }

  return response.json();
}

export async function updateProduct(
  id: number,
  data: Partial<ProductCreate>
): Promise<Product> {
  const response = await fetch(`${API_URL}/productos/${id}`, {
    method: "PUT",
    headers: getHeaders(),
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error("Error al actualizar producto");
  }

  return response.json();
}

export async function deleteProduct(id: number): Promise<void> {
  const response = await fetch(`${API_URL}/productos/${id}`, {
    method: "DELETE",
    headers: getHeaders(),
  });

  if (!response.ok) {
    throw new Error("Error al eliminar producto");
  }
}